package app.softwork.composetodo

import app.softwork.composetodo.dto.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*
import kotlinx.datetime.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.*
import kotlin.coroutines.cancellation.*

sealed interface API {
    class LoggedOut(private val client: HttpClient) : API {
        suspend fun register(newUser: User.New): LoggedIn? {
            val response = client.post("/users") {
                setBody(newUser using User.New.serializer())
            }
            return if(response.status == HttpStatusCode.BadRequest) {
                null
            } else {
                LoggedIn(Token.serializer() by response, client)
            }
        }

        @Throws(IOException::class, CancellationException::class)
        suspend fun login(
            username: String,
            password: String
        ): LoggedIn? {
            val response = client.post("/refreshToken") {
                basicAuth(username, password)
            }
            return if(response.status == HttpStatusCode.Unauthorized) {
                null
            } else {
                LoggedIn( Token.serializer() by response, client)
            }
        }

        @Throws(IOException::class, CancellationException::class)
        suspend fun silentLogin(): LoggedIn? {
            val response = client.get("/refreshToken")
            return if(response.status == HttpStatusCode.BadRequest) {
                null
            } else {
                LoggedIn(Token.serializer() by response, client)
            }
        }
    }

    class LoggedIn(private var token: Token, private val client: HttpClient) : API {
        private suspend fun HttpRequestBuilder.addToken() {
            if (Clock.System.now() > token.payload.expiredAt) {
                token = Token.serializer() by client.get("/refreshToken")
            }
            header(HttpHeaders.Authorization, "Bearer ${token.content}")
        }

        @Throws(IOException::class, CancellationException::class)
        suspend fun logout() {
            client.delete("/refreshToken") {
                addToken()
            }
        }

        @Throws(IOException::class, CancellationException::class)
        suspend fun getMe(): User = User.serializer() by client.get("/me") {
            addToken()
        }

        @Throws(IOException::class, CancellationException::class)
        suspend fun updateMe(user: User): User = User.serializer() by client.put("/me") {
            setBody(user using User.serializer())
            addToken()
        }

        @Throws(IOException::class, CancellationException::class)
        suspend fun deleteMe() {
            client.delete("/me") {
                addToken()
            }
        }

        @Throws(IOException::class, CancellationException::class)
        suspend fun getTodos() = ListSerializer(TodoDTO.serializer()) by client.get("/todos") {
            addToken()
        }

        @Throws(IOException::class, CancellationException::class)
        suspend fun getTodo(todoID: TodoDTO.ID) = TodoDTO.serializer() by client.get("/todos/${todoID.id}") {
            addToken()
        }

        @Throws(IOException::class, CancellationException::class)
        suspend fun createTodo(todo: TodoDTO) = TodoDTO.serializer() by client.post("/todos") {
            setBody(todo using TodoDTO.serializer())
            addToken()
        }

        @Throws(IOException::class, CancellationException::class)
        suspend fun updateTodo(todoID: TodoDTO.ID, todo: TodoDTO) =
            TodoDTO.serializer() by client.put("/todos/${todoID.id}") {
                setBody(todo using TodoDTO.serializer())
                addToken()
            }

        @Throws(IOException::class, CancellationException::class)
        suspend fun deleteTodo(todoID: TodoDTO.ID) {
            client.delete("/todos/${todoID.id}") {
                addToken()
            }
        }
    }

    companion object {
        private val json: Json = Json
        infix fun <T> T.using(serializer: KSerializer<T>) =
            json.encodeToString(serializer, this)

        suspend infix fun <T> KSerializer<T>.by(response: HttpResponse): T =
            json.decodeFromString(this, response.bodyAsText())
    }
}
