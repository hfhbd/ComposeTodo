package app.softwork.composetodo.repository

import app.softwork.composetodo.models.Todo

interface TodoRepository {
    suspend fun getRemote(): List<Todo>
    suspend fun sync(): List<Todo>
    suspend fun deleteAll()
    suspend fun delete(todo: Todo)
}
