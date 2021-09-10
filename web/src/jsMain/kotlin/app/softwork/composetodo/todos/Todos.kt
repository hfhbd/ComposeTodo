package app.softwork.composetodo.todos

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.*
import app.softwork.composetodo.dto.*
import app.softwork.composetodo.dto.Todo
import kotlinx.coroutines.*
import kotlinx.datetime.*
import org.jetbrains.compose.web.dom.*
import kotlin.time.*

@ExperimentalTime
class TodosViewModel(val api: API.LoggedIn) {
    var todos by mutableStateOf(emptyList<Todo>())
        private set

    init {
        scope.launch {
            todos = api.getTodos()
        }
    }

    fun add(todo: Todo) {
        todos += todo
        scope.launch {
            delay(3.seconds)
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

@ExperimentalTime
@Composable
fun Todos(viewModel: TodosViewModel) {
    NewTodo(NewTodoViewModel(viewModel.api) {
        viewModel.add(it)
    })
    H1 {
        Text("Todos")
    }
    if (viewModel.todos.isEmpty()) {
        Text("No Todos created")
    } else {
        Table(data = viewModel.todos, key = { it.id }) { _, todo ->
            rowColor = when {
                todo.finished -> Color.Success
                todo.until?.let {
                    it <= Clock.System.now()
                } ?: false -> Color.Warning
                else -> null
            }
            column("Title") {
                Text(todo.title)
            }
            column("") {
                Button("Delete", color = Color.Danger) {
                    viewModel.delete(todo)
                }
            }
        }
    }
}
