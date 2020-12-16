package com.example.composetodo.repository

import com.example.composetodo.TodoRest
import com.example.composetodo.TodoRest.Companion.todoRest
import com.example.composetodo.models.Todo
import kotlinx.datetime.Clock

class TodoRepository(
    private val dao: TodoDao,
    private val rest: TodoRest = todoRest()
) {
    suspend fun delete(todo: Todo) {
        dao.delete(todo)
        rest.delete(todo.toDTO())
    }

    suspend fun get(): List<Todo> = dao.getAll()

    suspend fun sync(): List<Todo> {
        val remoteRest = rest.getModified(Clock.System.now()) ?: emptyList()
        val remote = remoteRest.map { Todo(it) }
        val local = dao.getAll()
        val new = local - remote
        dao.insert(remote)
        return dao.getAll()
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}
