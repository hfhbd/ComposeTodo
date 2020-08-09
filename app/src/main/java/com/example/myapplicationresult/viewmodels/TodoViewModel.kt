package com.example.myapplicationresult.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myapplicationresult.HttpClient
import com.example.myapplicationresult.models.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer

class TodoViewModel {
    private val client = HttpClient("https://jsonplaceholder.typicode.com/todos")
    var todos by mutableStateOf(emptyList<Todo>())
    fun loadNew() {
        GlobalScope.launch(Dispatchers.IO) {
            val newTodos = getNewTodos()
            withContext(Dispatchers.Main) {
                todos = newTodos
            }
        }
    }
    private suspend fun deleteTodo(id: Int) = client.delete("/$id").getOrDefault(Unit)

    private suspend fun getNewTodos() =
        client.get("/", ListSerializer(Todo.serializer())).getOrDefault(emptyList())
}
