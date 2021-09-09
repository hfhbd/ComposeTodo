package app.softwork.composetodo.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import app.softwork.composetodo.models.*
import app.softwork.composetodo.viewmodels.*
import kotlinx.datetime.*

@Composable
fun <T : Todo> NewTodo(viewModel: TodoViewModel<T>) {
    var title by remember { mutableStateOf("") }
    var until: Instant? by remember { mutableStateOf(null) }

    Column {
        Row {
            TextField(value = title, onValueChange = {
                title = it
            }, label = {
                Text("Title")
            })

            Button(onClick = {
                viewModel.create(title = title, until = until)
                title = ""
                until = null
            }) {
                Text("Create")
            }
        }
    }
}