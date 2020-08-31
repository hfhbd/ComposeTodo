package com.example.composetodo.repository

import com.example.composetodo.models.Todo
import com.example.composetodo.repository.TodoRest.Companion.todoRest
import java.time.OffsetDateTime

class TodoRepository(
    private val dao: TodoDao,
    private val rest: TodoRest = todoRest()
) {
    suspend fun delete(todo: Todo) {
        dao.delete(todo)
        rest.delete(todo)
    }

    suspend fun get(): List<Todo> = dao.getAll()

    suspend fun sync(): List<Todo> {
        val remote = rest.getModified(OffsetDateTime.now()) ?: emptyList()
        val local = dao.getAll()
        val new = local - remote
        dao.insert(remote)
        return dao.getAll()
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}
