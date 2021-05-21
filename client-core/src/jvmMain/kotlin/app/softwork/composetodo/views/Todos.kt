package app.softwork.composetodo.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import app.softwork.composetodo.models.*
import app.softwork.composetodo.viewmodels.*

@Composable
fun <T : Todo> Todos(viewModel: TodoViewModel<T>) {
    Row {
        Button(viewModel::clear) { Text("Delete All") }
        Button(viewModel::loadNew) { Text("Load from Server") }
    }
    Row {
        NewTodo(viewModel)
    }
    Column {
        viewModel.todos.forEach { todo ->
            TodoRow(todo)
        }
    }
}
