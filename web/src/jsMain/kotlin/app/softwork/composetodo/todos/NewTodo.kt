package app.softwork.composetodo.todos

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.*
import app.softwork.composetodo.dto.*
import kotlinx.coroutines.*
import kotlinx.datetime.*
import kotlinx.uuid.*
import org.jetbrains.compose.web.attributes.*

class NewTodoViewModel(val api: API.LoggedIn, val created: (Todo) -> Unit) {
    var title by mutableStateOf("")
    var until by mutableStateOf("")

    val disabledButton: Boolean
        get() = title.isEmpty() || until.isEmpty()

    fun createTodo() {
        scope.launch {
            val newTodo = Todo(
                id = UUID(),
                title = title,
                finished = false,
                until = until.takeIf { it.isNotBlank() }?.let {
                    LocalDateTime.parse(until).toInstant(TimeZone.currentSystemDefault())
                },
                recordChangeTag = null
            )
            api.createTodo(newTodo)
            created(newTodo)
        }
    }
}

@Composable
fun NewTodo(viewModel: NewTodoViewModel) {
    Input(
        type = InputType.Text,
        label = "Title",
        placeholder = "Hello World",
        value = viewModel.title
    ) {
        viewModel.title = it.value
    }
    Input(
        type = InputType.DateTimeLocal,
        label = "Finish Date",
        placeholder = "yyyy-mm-dd",
        value = viewModel.until
    ) {
        viewModel.until = it.value
    }
    Button("Create new Todo", attrs = {
        if (viewModel.disabledButton) {
            disabled()
        }
    }) {
        viewModel.createTodo()
    }
}
