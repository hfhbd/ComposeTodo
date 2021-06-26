//
//  ContentView.swift
//  Shared
//
//  Created by Philip Wedemann on 17.05.21.
//

import SwiftUI
import CoreData
import shared

final actor RefreshTokenStorage: CookiesStorage {
    @AppStorage("refreshtoken") private var refreshToken: String?

    func addCookie(requestUrl: Url, cookie: Cookie) async throws -> KotlinUnit {
        refreshToken = cookie.value
        return KotlinUnit()
    }

    func get(requestUrl: Url) async throws -> [Cookie] {
        guard let refreshToken = refreshToken else {
            return []
        }

        return [Cookie(name: "SESSION", value: refreshToken, encoding: .uriEncoding, maxAge: 0, expires: nil, domain: nil, path: nil, secure: false, httpOnly: true, extensions: [:])]
    }


    nonisolated func close() {
    }
}


final class ViewModel: ObservableObject {

    @Published private (set) var api: API = ClientKt.api(cookiesStorage: RefreshTokenStorage())


    func login(username: String, password: String) async throws {
        guard let api = api as? API.LoggedOut else { return }
        self.api = try await api.login(username: username, password: password)
    }

    func refreshTodos() async throws -> [Todo] {
        guard let api = api as? API.LoggedIn else { return [] }
        return try await api.getTodos()
    }
}

struct Login: View {
    let login: (String, String) async throws -> Void

    @State private var username: String = ""
    @State private var password: String = ""
    @State private var error: Error? = nil

    var body: some View {
        Form {
            TextField("Username", text: $username)
            SecureField("Password", text: $password) {
                async {
                    do {
                        try await login(username, password)
                    } catch {
                        self.error = error
                    }
                }
            }
            if(error != nil) {
                Text("Error \(error.debugDescription). Please try it again.")
            }
        }
    }
}


struct ContentView: View {
    @ObservedObject private (set) var viewModel: ViewModel

    var body: some View {
        NavigationView {
            if(viewModel.api is API.LoggedOut) {
                TabView {
                    Login(login: viewModel.login).tabItem {
                        Label("Login", systemImage: "person")
                    }.navigationTitle("Login")
                }
            } else {
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

    var body: some View {
        List {
            ForEach(items) { item in
                TodoItemRow(item: item)
            }
            .onDelete(perform: deleteItems)
        }
        .toolbar {
            #if os(iOS)
            EditButton()
            #endif

            NavigationLink(destination: NewItem { title, until in
                    let new = newTodo()
                new.title = title
                new.finished = false
                new.until = until
                save()
            }) {
                Label("Add Item", systemImage: "plus")
            }
        }.refreshable {
            let newTodos = (try? await viewModel.refreshTodos()) ?? []
            saveNewTodos(items: items, newTodos: newTodos)
        }.navigationTitle("Items")
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
                item.id == todo.id.toNsUUID()
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
            offsets.map { items[$0] }.forEach(viewContext.delete)
        }
        save()
    }
}

struct NewItem: View {
    let save: (String, Date) -> Void

    @Environment(\.dismiss) var dismiss


    @State private var title: String = ""
    @State private var until: Date = Date.now

    var body: some View {
        Form {
            TextField("Title", text: $title)
            DatePicker("Until", selection: $until)
        }.navigationTitle(self.title.ifBlank { "New Todo" })
            .toolbar {
                Button("Save") {
                    save(self.title, self.until)
                    dismiss()
                }
            }
    }
}

extension String {
    func ifBlank(fallback: () -> String) -> String {
        if(isEmpty) {
            return fallback()
        } else {
            return self
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView(viewModel: ViewModel()).environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
    }
}

struct TodoItemRow: View {
    let item: TodoItem

    var body: some View {
        VStack {
            Text(item.title!)
        }
    }
}
