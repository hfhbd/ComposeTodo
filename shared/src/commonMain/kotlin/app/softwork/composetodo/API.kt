package app.softwork.composetodo

import app.softwork.composetodo.dto.Todo
import app.softwork.composetodo.dto.User
import kotlinx.uuid.UUID

interface API {
    suspend fun login(userName: String, password: String): Boolean
    val user: User

    suspend fun getUser(): User
    suspend fun createUser(user: User): User
    suspend fun updateUser(user: User): User
    suspend fun deleteUser()

    suspend fun getTodos(): List<Todo>
    suspend fun getTodo(todoID: UUID): Todo
    suspend fun createTodo(todo: Todo): Todo
    suspend fun updateTodo(todoID: UUID, todo: Todo): Todo
    suspend fun deleteTodo(todoID: UUID)


    companion object
}