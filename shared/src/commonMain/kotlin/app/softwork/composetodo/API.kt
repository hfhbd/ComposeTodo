package app.softwork.composetodo

import app.softwork.composetodo.dto.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.InternalAPI
import io.ktor.utils.io.errors.*
import kotlinx.datetime.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.*
import kotlinx.uuid.*
import kotlin.coroutines.cancellation.*

sealed class API {
    private val json: Json = Json

    class LoggedOut(private val client: HttpClient) : API() {
        suspend fun register(newUser: User.New): LoggedIn? {
            val token = try {
                Token.serializer() by client.post("/users") {
                    body = newUser using User.New.serializer()
                }
            } catch (e: ClientRequestException) {
                if (e.response.status == HttpStatusCode.BadRequest) {
                    return null
                } else {
                    throw e
                }
            }

            return LoggedIn(token, client)
        }

        @OptIn(InternalAPI::class)
        @Throws(IOException::class, CancellationException::class)
        suspend fun login(
            username: String,
            password: String
        ): LoggedIn? {
            val token = try {
                Token.serializer() by client.post("/refreshToken") {
                    header(
                        HttpHeaders.Authorization,
                        "Basic ${"$username:$password".encodeBase64()}"
                    )
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

        @Throws(IOException::class, CancellationException::class)
        suspend fun silentLogin(): LoggedIn? {
            val token = try {
                Token.serializer() by client.get("/refreshToken")
            } catch (e: ClientRequestException) {
                if (e.response.status == HttpStatusCode.BadRequest) {
                    null
                } else {
                    throw e
                }
            }
            return token?.let { LoggedIn(token, client) }
        }
    }

    class LoggedIn(private var token: Token, private val client: HttpClient) : API() {
        private suspend fun HttpRequestBuilder.addToken() {
            if (Clock.System.now() > token.payload.expiredAt) {
                token = Token.serializer() by client.get("/refreshToken")
            }
            header(HttpHeaders.Authorization, "Bearer ${token.content}")
        }

        @Throws(IOException::class, CancellationException::class)
        suspend fun logout() = client.delete<Unit>("/refreshToken") {
            addToken()
        }

        @Throws(IOException::class, CancellationException::class)
        suspend fun getMe(): User = User.serializer() by client.get("/me") {
            addToken()
        }

        @Throws(IOException::class, CancellationException::class)
        suspend fun updateMe(user: User): User = User.serializer() by client.put("/me") {
            body = user using User.serializer()
            addToken()
        }

        @Throws(IOException::class, CancellationException::class)
        suspend fun deleteMe() = client.delete<Unit>("/me") {
            addToken()
        }

        @Throws(IOException::class, CancellationException::class)
        suspend fun getTodos() =
            ListSerializer(Todo.serializer()) by client.get("/todos") {
                addToken()
            }

        @Throws(IOException::class, CancellationException::class)
        suspend fun getTodo(todoID: UUID) = Todo.serializer() by
                client.get("/todos/$todoID") {
                    addToken()
                }

        @Throws(IOException::class, CancellationException::class)
        suspend fun createTodo(todo: Todo) = Todo.serializer() by
                client.post("/todos") {
                    body = todo using Todo.serializer()
                    addToken()
                }

        @Throws(IOException::class, CancellationException::class)
        suspend fun updateTodo(todoID: UUID, todo: Todo) = Todo.serializer() by
                client.put("/todos/$todoID") {
                    body = todo using Todo.serializer()
                    addToken()
                }

        @Throws(IOException::class, CancellationException::class)
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
