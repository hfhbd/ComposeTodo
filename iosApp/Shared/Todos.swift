//
//  Todos.swift
//  composetodo (iOS)
//
//  Created by Philip Wedemann on 04.11.21.
//

import SwiftUI
import clients

struct Todos: View {
    init(viewModel: @autoclosure @escaping () -> TodoViewModel) {
        self._viewModel = StateObject(wrappedValue: viewModel())
    }
    
    @StateObject var viewModel: TodoViewModel

    @State private var todos = [Todo]()

    var body: some View {
        List(todos) { todo in
            TodoItemRow(item: todo)
        }.refreshable {
            viewModel.refresh()
        }.task {
            for await newTodos in viewModel.todos.stream([Todo].self) {
                self.todos = newTodos
            }
        }
    }
}

struct Todos_Previews: PreviewProvider {
    static var previews: some View {
        Todos(viewModel: TodoViewModel.init(repo: TestRepo()))
    }

    class TestRepo: TodoRepository {
        init() {

        }

        func create(title: String, until: Instant?) async throws { }

        func delete(todo: Todo) async throws { }

        func deleteAll() async throws { }

        func sync() async throws { }

        var todos: Flow {
            get {
                BuildersKt.emptyFlow()
            }
        }
    }
}
