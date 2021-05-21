package app.softwork.composetodo.viewmodels

import androidx.compose.runtime.*
import app.softwork.composetodo.models.*
import app.softwork.composetodo.repository.*
import kotlinx.coroutines.*
import kotlinx.datetime.*

class TodoViewModel<T: Todo>(private val scope: CoroutineScope, private val repo: TodoRepository<T>) {
    var todos by mutableStateOf(emptyList<Todo>())

    init {
        scope.launch(Dispatchers.Main) {
            todos = withContext(Dispatchers.IO) {
                repo.getRemote()
            }
        }
    }

    fun loadNew() {
        scope.launch(Dispatchers.Main) {
            todos = withContext(Dispatchers.IO) { repo.sync() }
        }
    }

    fun clear() {
        scope.launch(Dispatchers.IO) {
            repo.deleteAll()
        }
    }

    fun create(title: String, until: Instant?) {
        scope.launch(Dispatchers.IO) {
            repo.create(title = title, until = until)
            repo.sync()
        }
    }
}
