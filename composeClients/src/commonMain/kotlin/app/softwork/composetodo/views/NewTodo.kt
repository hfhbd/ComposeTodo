package app.softwork.composetodo.views

import androidx.compose.runtime.*
import app.softwork.composetodo.*
import app.softwork.composetodo.viewmodels.*
import kotlinx.datetime.*

@Composable
fun NewTodo(viewModel: TodoViewModel) {
    var title by remember { mutableStateOf("") }
    var until: Instant? by remember { mutableStateOf(null) }

    Column {
        Row {
            TextField(
                value = title, onValueChange = {
                    title = it
                }, label = "Title",
                placeholder = "To-do Title",
                isPassword = false
            )

            DateField(
                value = until, onValueChange = {
                    until = it
                }, label = "Until"
            )

            Button(title = "Create", enabled = title.isNotEmpty()) {
                viewModel.create(title = title, until = until)
                title = ""
                until = null
            }
        }
    }
}
