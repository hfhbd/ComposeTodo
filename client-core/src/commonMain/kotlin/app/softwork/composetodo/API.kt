package app.softwork.composetodo

import app.softwork.composetodo.dto.Todo
import app.softwork.composetodo.dto.User
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.uuid.UUID

operator fun API.Companion.invoke(
    client: HttpClient,
    json: Json = Json
) = object : API {
    override lateinit var user: User
    override suspend fun login(userName: String, password: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUser() =
        client.get<String>("/users/${user.id}").let {
            json.decodeFromString(User.serializer(), it)
        }

    override suspend fun createUser(user: User) = client.post<String>("/users") {
        body = json.encodeToString(User.serializer(), user)
    }.let {
        json.decodeFromString(User.serializer(), it)
    }

    override suspend fun updateUser(user: User) =
        client.put<String>("/users/${user.id}") {
            body = json.encodeToString(User.serializer(), user)
        }.let {
            json.decodeFromString(User.serializer(), it)
        }

    override suspend fun deleteUser() {
        client.delete<String>("/${user.id}")
    }

    override suspend fun getTodos() =
        client.get<String>("/users/${user.id}/todos").let {
            json.decodeFromString(ListSerializer(Todo.serializer()), it)
        }

    override suspend fun getTodo(todoID: UUID) =
        client.get<String>("/users/${user.id}/todos/$todoID").let {
            json.decodeFromString(Todo.serializer(), it)
        }

    override suspend fun createTodo(todo: Todo) =
        client.post<String>("/users/${user.id}/todos") {
            body = json.encodeToString(Todo.serializer(), todo)
        }.let {
            json.decodeFromString(Todo.serializer(), it)
        }

    override suspend fun updateTodo(todoID: UUID, todo: Todo) =
        client.put<String>("/users/${user.id}/todos/$todoID") {
            body = json.encodeToString(Todo.serializer(), todo)
        }.let {
            json.decodeFromString(Todo.serializer(), it)
        }

    override suspend fun deleteTodo(todoID: UUID) {
        client.delete<String>("/users/${user.id}/todos/$todoID")
    }
}
