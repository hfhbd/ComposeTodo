package app.softwork.composetodo.viewmodels

import app.softwork.composetodo.*
import app.softwork.composetodo.repository.*
import kotlinx.coroutines.*
import kotlinx.datetime.*

class TodoViewModel(private val scope: CoroutineScope, private val repo: TodoRepository) {
    val todos = repo.todos

    init {
        refresh()
    }

    fun refresh() {
        scope.launch(Dispatchers.Default) {
            repo.sync()
        }
    }

    fun deleteAll() {
        scope.launch(Dispatchers.Default) {
            repo.deleteAll()
        }
    }

    fun create(title: String, until: Instant?) {
        scope.launch(Dispatchers.Default) {
            repo.create(title = title, until = until)
        }
    }

    fun delete(todo: Todo) {
        scope.launch(Dispatchers.Default) {
            repo.delete(todo)
        }
    }
}
