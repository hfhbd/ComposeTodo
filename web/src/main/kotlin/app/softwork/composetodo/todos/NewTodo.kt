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
        label = "Until",
        placeholder = "yyyy-mm-dd",
        value = until
    ) {
        until = it.value
    }

    Button("Create new to-do", disabled = title.isEmpty()) {
        val untilInstant = until.takeIf { it.isNotBlank() }
            ?.let { LocalDateTime.parse(until).toInstant(TimeZone.currentSystemDefault()) }
        viewModel.create(title, untilInstant)
    }
}
