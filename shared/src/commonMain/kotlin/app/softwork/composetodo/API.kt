package app.softwork.composetodo

import app.softwork.composetodo.dto.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.InternalAPI
import kotlinx.datetime.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.*
import kotlinx.uuid.*

sealed class API {
    private val json: Json = Json

    data class LoggedOut(val client: HttpClient) : API() {
        suspend fun register(newUser: User.New): LoggedIn = LoggedIn(Token.serializer() by client.post("/users") {
            body = newUser using User.New.serializer()
        }, client)

        @OptIn(InternalAPI::class)
        suspend fun login(
            username: String,
            password: String
        ): LoggedIn? {
            val token = try {
                Token.serializer() by client.get("/refreshToken") {
                    header(HttpHeaders.Authorization, "Basic ${"$username:$password".encodeBase64()}")
                }
            } catch (e: ClientRequestException) {
                if (e.response.status == HttpStatusCode.Unauthorized) {
                    return null
                } else {
                    throw e
                }
            }

            return LoggedIn(token, client)
        }
    }

    data class LoggedIn(private var token: Token, val client: HttpClient) : API() {
        private suspend fun HttpRequestBuilder.addToken() {
            if (Clock.System.now() > token.payload.expiredAt) {
                token = Token.serializer() by client.get("/refreshToken")
            }
            header(HttpHeaders.Authorization, "Bearer ${token.content}")
        }

        suspend fun logout() = client.delete<Unit>("/refreshToken") {
            addToken()
        }

        suspend fun getMe(): User = User.serializer() by client.get("/me") {
            addToken()
        }

        suspend fun updateMe(user: User): User = User.serializer() by client.put("/me") {
            body = user using User.serializer()
            addToken()
        }

        suspend fun deleteMe() = client.delete<Unit>("/me") {
            addToken()
        }

        suspend fun getTodos() =
            ListSerializer(Todo.serializer()) by client.get("/todos") {
                addToken()
            }

        suspend fun getTodo(todoID: UUID) = Todo.serializer() by
                client.get("/todos/$todoID") {
                    addToken()
                }

        suspend fun createTodo(todo: Todo) = Todo.serializer() by
                client.post("/todos") {
                    body = todo using Todo.serializer()
                    addToken()
                }

        suspend fun updateTodo(todoID: UUID, todo: Todo) = Todo.serializer() by
                client.put("/todos/$todoID") {
                    body = todo using Todo.serializer()
                    addToken()
                }

        suspend fun deleteTodo(todoID: UUID) =
            client.delete<Unit>("/todos/$todoID") {
                addToken()
            }
    }

    internal infix fun <T> T.using(serializer: KSerializer<T>) =
        json.encodeToString(serializer, this)

    internal infix fun <T> KSerializer<T>.by(response: String): T =
        json.decodeFromString(this, response)
}
