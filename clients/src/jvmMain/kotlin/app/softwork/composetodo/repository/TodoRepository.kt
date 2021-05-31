package app.softwork.composetodo.repository

import app.softwork.composetodo.models.*
import kotlinx.datetime.*

interface TodoRepository<T: Todo> {
    suspend fun sync(): List<T>
    suspend fun deleteAll()
    suspend fun delete(todo: T)
    suspend fun create(title: String, until: Instant?)
}
