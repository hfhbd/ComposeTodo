package app.softwork.composetodo.todos

import app.softwork.composetodo.*
import app.softwork.composetodo.dto.*
import kotlinx.coroutines.*
import kotlinx.datetime.*
import kotlinx.html.*
import kotlinx.uuid.*
import react.*

@HtmlTagMarker
fun RBuilder.newTodo(onDone: () -> Unit) = child(NewTodo) {
    attrs {
        this.onDone = onDone
    }
}

private external interface NewTodoProps : RProps {
    var onDone: () -> Unit
}

private val NewTodo = functionalComponent<NewTodoProps> { props ->
    var title by useState("")
    var until by useState("")
    var enableCreateButton by useState(false)

    Input(type = InputType.text, label = "Title", placeholder = "Hello World", value = title) {
        val newValue = it.value
        enableCreateButton = newValue.isNotEmpty() && until.isNotEmpty()
        title = newValue
    }
    Input(
        type = InputType.dateTimeLocal,
        label = "Finish Date",
        placeholder = "yyyy-mm-dd",
        value = until
    ) {
        val newValue = it.value
        enableCreateButton = title.isNotEmpty() && newValue.isNotEmpty()
        until = newValue
    }
    Button("Create new Todo", attrs = {
        disabled = !enableCreateButton
    }) {
        scope.launch {
            api.createTodo(
                Todo(
                    id = SecureRandom.nextUUID(),
                    title = title,
                    finished = false,
                    until = LocalDateTime.parse(until)
                )
            )
            props.onDone()
        }
    }
}