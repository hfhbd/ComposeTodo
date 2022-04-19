package app.softwork.composetodo.viewmodels

import app.softwork.composetodo.*
import app.softwork.composetodo.repository.*
import kotlinx.coroutines.*
import kotlinx.datetime.*

class TodoViewModel(private val repo: TodoRepository): ViewModel() {
    val todos = repo.todos

    init {
        refresh()
    }

    fun refresh() {
        lifecycleScope.launch(Dispatchers.Default) {
            repo.sync()
        }
    }

    fun deleteAll() {
        lifecycleScope.launch(Dispatchers.Default) {
            repo.deleteAll()
        }
    }

    fun create(title: String, until: Instant?) {
        lifecycleScope.launch(Dispatchers.Default) {
            repo.create(title = title, until = until)
        }
    }

    fun delete(todo: Todo) {
        lifecycleScope.launch(Dispatchers.Default) {
            repo.delete(todo)
        }
    }
}
