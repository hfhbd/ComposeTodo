package com.example.composetodo.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetodo.models.Todo
import com.example.composetodo.repository.TodoRepository
import kotlinx.coroutines.*

class TodoViewModel(private val scope: CoroutineScope, private val repo: TodoRepository) {
    var todos by mutableStateOf(emptyList<Todo>())

    init {
        println("called init")
        scope.launch(Dispatchers.Main) {
            println("start init")
            todos = withContext(Dispatchers.IO) {
                println("execute init")
                repo.get().also {
                    println("end init with $it")
                }
            }
            println("init todos = $todos")
        }
    }

    fun loadNew() {
        scope.launch(Dispatchers.Main) {
            todos = withContext(Dispatchers.IO) { repo.getRemote() }
            println("loadNew $todos")
        }
    }

    fun clear() {
        scope.launch(Dispatchers.IO) {
            repo.deleteAll()
        }
    }
}
