package app.softwork.composetodo.views

import androidx.compose.runtime.*
import app.softwork.composetodo.*
import app.softwork.composetodo.viewmodels.*

@Composable
fun Todos(viewModel: TodoViewModel) {
    Row {
        Button("Delete All", enabled = true, viewModel::deleteAll)
        Button("Load from Server", enabled = true, viewModel::refresh)
    }
    Row {
        NewTodo(viewModel)
    }
    val todos by remember { viewModel.todos }.collectAsState(emptyList())
    Column {
        todos.forEach { todo ->
            TodoRow(todo)
        }
    }
}
