package app.softwork.composetodo.todos

import androidx.compose.runtime.*
import androidx.compose.web.attributes.*
import app.softwork.composetodo.*
import app.softwork.composetodo.dto.*
import kotlinx.coroutines.*
import kotlinx.datetime.*
import kotlinx.uuid.*

class NewTodoViewModel(val api: API.LoggedIn, val onDone: () -> Unit) {
    var title by mutableStateOf("")
    var until by mutableStateOf("")

    val disabledButton: Boolean
        get() {
            println("disabledButton $title $until s")
            return title.isEmpty() || until.isEmpty()
        }

    fun createTodo() {
        scope.launch {
            api.createTodo(
                Todo(
                    id = SecureRandom.nextUUID(),
                    title = title,
                    finished = false,
                    until = LocalDateTime.parse(until)
                )
            )
            onDone()
        }
    }
}

@Composable
fun NewTodo(viewModel: NewTodoViewModel) {
    input(type = InputType.Text, label = "Title", placeholder = "Hello World", value = viewModel.title) {
        viewModel.title = it.value
    }
    input(
        type = InputType.DateTimeLocal,
        label = "Finish Date",
        placeholder = "yyyy-mm-dd",
        value = viewModel.until
    ) {
        viewModel.until = it.value
    }
    button("Create new Todo", attrs = {
        disabled(viewModel.disabledButton)
    }) {
        viewModel.createTodo()
    }
}
