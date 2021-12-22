package app.softwork.composetodo.todos

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.viewmodels.*
import kotlinx.datetime.*
import org.jetbrains.compose.web.attributes.*

@Composable
fun NewTodo(viewModel: TodoViewModel) {
    var title by remember { mutableStateOf("") }
    var until by remember { mutableStateOf("") }
    Input(
        type = InputType.Text,
        label = "Title",
        placeholder = "Hello World",
        value = title
    ) {
        title = it.value
    }
    Input(
        type = InputType.DateTimeLocal,
        label = "Finish Date",
        placeholder = "yyyy-mm-dd",
        value = until
    ) {
        until = it.value
    }

    Button("Create new Todo", disabled = title.isEmpty()) {
        viewModel.create(title, until.takeIf { it.isNotBlank() }?.let {
            LocalDateTime.parse(until).toInstant(TimeZone.currentSystemDefault())
        })
    }
}
