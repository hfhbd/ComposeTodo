package app.softwork.composetodo.repository

import app.softwork.composetodo.models.Todo

interface TodoRepository<T: Todo> {
    suspend fun getRemote(): List<T>
    suspend fun sync(): List<T>
    suspend fun deleteAll()
    suspend fun delete(todo: T)
}
