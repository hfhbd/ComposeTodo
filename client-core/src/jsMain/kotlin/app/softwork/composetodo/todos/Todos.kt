package app.softwork.composetodo.todos

import app.softwork.composetodo.*
import app.softwork.composetodo.dto.*
import kotlinx.coroutines.*
import kotlinx.datetime.*
import kotlinx.datetime.Clock
import kotlinx.html.*
import kotlinx.uuid.*
import react.*
import react.dom.*
import kotlin.time.*

@HtmlTagMarker
fun RBuilder.todos() = child(Todos) { }

private val Todos = functionalComponent<RProps> {
    var todos by useState(emptyList<Todo>())
    useEffectWithCleanup(emptyList()) {
        val job = scope.launch {
            todos = api.getTodos()
        }
        job::cancel
    }
    newTodo {
        scope.launch {
            todos = api.getTodos()
        }
    }
    h1 {
        +"Todos"
    }
    if (todos.isEmpty()) {
        +"No Todos created"
    } else {
        Table(todos, { it.id.toString() }) { todo ->
            rowColor = when {
                todo.finished -> Color.Success
                todo.until?.let {
                    it.toInstant(TimeZone.currentSystemDefault()) <= Clock.System.now()
                } ?: false -> Color.Warning
                else -> null
            }
            cell("Title") {
                +todo.title
            }
            cell("") {
                Button("Delete") {
                    scope.launch {
                        api.deleteTodo(todo.id)
                    }
                }
            }
        }
    }
}