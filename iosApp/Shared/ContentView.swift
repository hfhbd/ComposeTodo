import SwiftUI
import CoreData
import shared

struct Login: View {
    let login: (String, String) async throws -> Void
    let silentLogin: () -> Void
    
    @State private var username: String = ""
    @State private var password: String = ""
    @State private var error: String? = nil

    @FocusState private var focus: Fields?

    private enum Fields: Hashable {
        case username, password
    }

    @MainActor
    private func submit() async {
        focus = nil
        do {
            try await login(username, password)
        } catch let error as Errors {
            switch error {
            case .WrongPassword:
                self.error = "Wrong password"
                focus = .password
            }
        } catch {
            self.error = error.localizedDescription
        }
    }

    var body: some View {
        Form {
            TextField("Username", text: $username, onCommit: {
                focus = .password
            }).focused($focus, equals: .username)
            SecureField("Password", text: $password) {
                Task {
                    await submit()
                }
            }.focused($focus, equals: .password)

            if let error = error {
                Text(error)
            }
        }.toolbar {
            Button("Login") {
                Task {
                    await submit()
                }
            }
        }.onAppear {
            focus = .username
            silentLogin()
        }
    }
}

final class SwiftViewModel: ObservableObject {
    let viewModel: ViewModel
    init(viewModel: ViewModel) {
        self.viewModel = viewModel
        viewModel.loginState.key
        viewModel.data(subscription: {
            
        })
    }
    
    @Published private (set) var login: LoginState = LoginState.loggedout
    @Published private (set) var data: RequestResult.KeyValueObservingPublisher
}


struct ContentView: View {
    
    var body: some View {
        if (viewModel.log is API.LoggedOut) {
            TabView {
                NavigationView {
                    Login(login: viewModel.login) {
                        Task { try? await viewModel.trySilentLogin() }
                    }
                            .navigationTitle("Login")
                }.tabItem {
                    Label("Login", systemImage: "person")
                }
            }
        } else {
            NavigationView {
                Todos(viewModel: viewModel).navigationTitle("Todos")
            }
        }
    }
}

struct Todos: View {
    @Environment(\.managedObjectContext) private var viewContext

    @ObservedObject private (set) var viewModel: ViewModel

    @FetchRequest(
            sortDescriptors: [NSSortDescriptor(keyPath: \TodoItem.title, ascending: true)],
            animation: .default)
    private var items: FetchedResults<TodoItem>

    @State private var selection: Set<TodoItem> = []

    @State private var createNewTodo = false
    @State var editMode: EditMode = .inactive
    var body: some View {
        List(selection: $selection) {
            ForEach(items) { item in
                TodoItemRow(item: item)
            }
                    .onDelete(perform: deleteItems)
        }
                .toolbar {

                    #if os(iOS)
                    ToolbarItem(placement: .navigationBarLeading) {
                        EditButton()
                    }
                    #endif

                    ToolbarItem(placement: .navigationBarTrailing) {

                        if editMode == .inactive {
                            Button(action: {
                                self.createNewTodo = true
                            }) {
                                Label("Add Item", systemImage: "plus")
                            }
                        } else {
                            Button(action: {
                                withAnimation {
                                    selection.forEach(viewContext.delete)
                                }
                                save()
                                selection = []
                            }) {
                                Label("Delete", systemImage: "trash")
                            }
                        }
                    }
                }
                .refreshable {
                    let newTodos = (try? await viewModel.refreshTodos()) ?? []
                    saveNewTodos(items: items, newTodos: newTodos)
                }.sheet(isPresented: self.$createNewTodo) {
                    self.createNewTodo = false
                } content: {
                    NavigationView {
                        NewItem { title, until in
                            let new = newTodo()
                            new.title = title
                            new.finished = false
                            new.until = until
                            save()
                        }
                    }
                }.navigationTitle("Items")
                .environment(\.editMode, $editMode)
    }

    private func save() {
        do {
            try viewContext.save()
        } catch {
            // Replace this implementation with code to handle the error appropriately.
            // fatalError() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
            let nsError = error as NSError
            fatalError("Unresolved error \(nsError), \(nsError.userInfo)")
        }
    }

    private func saveNewTodos(items: FetchedResults<TodoItem>, newTodos todos: [Todo]) {
        todos.forEach { todo in
            let found = items.first(where: { item in
                let itemID = item.id
                let todoID = todo.id.toNsUUID()
                print("\(itemID) \(todoID)")
                return itemID == todoID
            }) ?? newTodo()

            found.title = todo.title
            found.finished = todo.finished
            found.until = todo.until?.toNSDate()
        }

        save()
    }

    private func newTodo() -> TodoItem {
        let new = TodoItem(context: viewContext)
        new.id = UUID()
        return new
    }

    private func deleteItems(offsets: IndexSet) {
        withAnimation {
            offsets.map {
                items[$0]
            }.forEach(viewContext.delete)
        }
        save()
    }
}

struct NewItem: View {
    let save: (String, Date) -> Void

    @Environment(\.dismiss) var dismiss
    @FocusState private var focus: Bool

    @State private var title: String = ""
    @State private var until: Date = Date.now

    var body: some View {
        Form {
            TextField("Title", text: $title).focused($focus)
            DatePicker("Until", selection: $until)
        }.onAppear {
                    focus = true
                }
                .navigationTitle(self.title.ifBlank {
                    "New Todo"
                })
                .toolbar {
                    Button("Save") {
                        save(self.title, self.until)
                        focus = false
                        dismiss()
                    }
                }
    }
}

extension String {
    func ifBlank(fallback: () -> String) -> String {
        if (isEmpty) {
            return fallback()
        } else {
            return self
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView(viewModel: ViewModel(api: TestAPI()))
                .environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
    }
}

class TestAPI: shared.API {
}

struct TodoItemRow: View {
    let item: TodoItem

    var body: some View {
        VStack {
            Text(item.title!)
        }
    }
}
