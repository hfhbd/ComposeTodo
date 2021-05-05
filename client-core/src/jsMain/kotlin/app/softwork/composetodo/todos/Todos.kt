package app.softwork.composetodo.todos

import androidx.compose.runtime.*
import androidx.compose.web.elements.*
import app.softwork.composetodo.*
import app.softwork.composetodo.dto.*
import kotlinx.coroutines.*
import kotlinx.datetime.*

class TodosViewModel(val api: API.LoggedIn) {
    var todos by mutableStateOf(emptyList<Todo>())

    init {
        scope.launch {
            todos = api.getTodos()
        }
    }

    fun refresh() {
        scope.launch {
            todos = api.getTodos()
        }
    }

    fun delete(todo: Todo) {
        scope.launch {
            api.deleteTodo(todo.id)
            refresh()
        }
    }
}

@Composable
fun Todos(viewModel: TodosViewModel) {
    NewTodo(NewTodoViewModel(viewModel.api) { viewModel.refresh() })
    H1 {
        Text("Todos")
    }
    if (viewModel.todos.isEmpty()) {
        Text("No Todos created")
    } else {
        Table(viewModel.todos) { todo ->
            rowColor = when {
                todo.finished -> Color.Success
                todo.until?.let {
                    it.toInstant(TimeZone.currentSystemDefault()) <= Clock.System.now()
                } ?: false -> Color.Warning
                else -> null
            }
            cell("Title") {
                Text(todo.title)
            }
            cell("") {
                button("Delete") {
                    viewModel.delete(todo)
                }
            }
        }
    }
}
