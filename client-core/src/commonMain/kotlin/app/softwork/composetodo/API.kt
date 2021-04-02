package app.softwork.composetodo

import app.softwork.composetodo.dto.Todo
import app.softwork.composetodo.dto.User
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.uuid.UUID

operator fun API.Companion.invoke(
    client: HttpClient,
    json: Json = Json
) = object : API {
    private infix fun <T> T.using(serializer: KSerializer<T>) =
        json.encodeToString(serializer, this)

    private infix fun <T> KSerializer<T>.by(response: String): T =
        json.decodeFromString(this, response)

    override lateinit var user: User
    override suspend fun login(userName: String, password: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUser() = User.serializer() by client.get("/users/${user.id}")

    override suspend fun createUser(user: User) = User.serializer() by client.post("/users") {
        body = user using User.serializer()
    }

    override suspend fun updateUser(user: User) =
        User.serializer() by client.put("/users/${user.id}") {
            body = user using User.serializer()
        }

    override suspend fun deleteUser() = client.delete<Unit>("/${user.id}")

    override suspend fun getTodos() =
        ListSerializer(Todo.serializer()) by client.get("/users/${user.id}/todos")

    override suspend fun getTodo(todoID: UUID) = Todo.serializer() by
        client.get("/users/${user.id}/todos/$todoID")

    override suspend fun createTodo(todo: Todo) = Todo.serializer() by
        client.post("/users/${user.id}/todos") {
            body = todo using Todo.serializer()
        }

    override suspend fun updateTodo(todoID: UUID, todo: Todo) = Todo.serializer() by
        client.put("/users/${user.id}/todos/$todoID") {
            body = todo using Todo.serializer()
        }

    override suspend fun deleteTodo(todoID: UUID) =
        client.delete<Unit>("/users/${user.id}/todos/$todoID")
}
