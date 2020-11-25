package com.example.composetodo

import com.example.composetodo.dto.Todo
import com.example.composetodo.dto.User
import kotlinx.uuid.UUID

interface API {
    suspend fun getUsers(): List<User>
    suspend fun getUser(userID: UUID): User
    suspend fun createUser(user: User): User
    suspend fun updateUser(userID: UUID, user: User): User
    suspend fun deleteUser(userID: UUID)

    suspend fun getTodos(userID: UUID): List<Todo>
    suspend fun getTodo(userID: UUID, todoID: UUID): Todo
    suspend fun createTodo(userID: UUID, todo: Todo): Todo
    suspend fun updateTodo(userID: UUID, todoID: UUID, todo: Todo): Todo
    suspend fun deleteTodo(userID: UUID, todoID: UUID)
}