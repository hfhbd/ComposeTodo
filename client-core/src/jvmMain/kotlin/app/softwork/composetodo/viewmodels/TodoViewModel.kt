package app.softwork.composetodo.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.softwork.composetodo.models.Todo
import app.softwork.composetodo.repository.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            println("loadNew $todos")
        }
    }

    fun clear() {
        scope.launch(Dispatchers.IO) {
            repo.deleteAll()
        }
    }
}
