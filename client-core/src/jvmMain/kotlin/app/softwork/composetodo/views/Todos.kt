package app.softwork.composetodo.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import app.softwork.composetodo.viewmodels.TodoViewModel

@Composable
fun Todos(viewModel: TodoViewModel) {
    Button(viewModel::clear) { Text("Clear")}
    Button(viewModel::loadNew) { Text("Remote") }
    Column {
        viewModel.todos.forEach { todo ->
            Text(todo.title)
        }
    }
}
