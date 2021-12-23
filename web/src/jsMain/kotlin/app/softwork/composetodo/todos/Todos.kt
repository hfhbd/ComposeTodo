package app.softwork.composetodo.todos

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.viewmodels.*
import kotlinx.datetime.*
import org.jetbrains.compose.web.dom.*
import kotlin.js.*

@Composable
fun Todos(viewModel: TodoViewModel) {
    NewTodo(viewModel)
    H1 {
        Text("Todos")
    }
    val todos by viewModel.todos.collectAsState(emptyList())

    if (todos.isEmpty()) {
        Text("No Todos created")
    } else {
        Table(data = todos.sortedBy { it.title }, key = { it.id }) { _, todo ->
            rowColor = when {
                todo.finished -> Color.Success
                todo.until?.let {
                    it <= Clock.System.now()
                } ?: false -> Color.Warning
                else -> null
            }
            column("Title") {
                Text(todo.title)
            }
            column("Until") {
                todo.until?.let {
                    Text(it.toDate().toLocaleString())
                }
            }
            column("") {
                Button("Delete", color = Color.Danger) {
                    viewModel.delete(todo)
                }
            }
        }
    }
}

private fun Instant.toDate(): Date = Date(toEpochMilliseconds())
