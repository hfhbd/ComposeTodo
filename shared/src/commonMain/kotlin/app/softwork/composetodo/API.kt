package app.softwork.composetodo

import app.softwork.composetodo.dto.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.utils.io.errors.*
import kotlinx.datetime.*
import kotlin.coroutines.cancellation.*

public sealed interface API {
    @Resource("/users")
    public object Users

    @Resource("/refreshToken")
    public object RefreshToken

    @Resource("/me")
    public object Me

    @Resource("/todos")
    public object Todos {
        @Resource("{id}")
        public class Id(public val parent: Todos = Todos, public val id: TodoDTO.ID)
    }

    public class LoggedOut(private val client: HttpClient) : API {
        @Throws(IOException::class, CancellationException::class)
        public suspend fun register(newUser: User.New): LoggedIn? {
            val response = client.post(Users) {
                contentType(ContentType.Application.Json)
                setBody(newUser)
            }
            return if (response.status == HttpStatusCode.BadRequest) {
                null
            } else {
                LoggedIn(response.body(), client)
            }
        }

        @Throws(IOException::class, CancellationException::class)
        public suspend fun login(
            username: String,
            password: String
        ): LoggedIn? {
            val response = client.post(RefreshToken) {
                basicAuth(username, password)
            }
            return if (response.status == HttpStatusCode.Unauthorized) {
                null
            } else {
                LoggedIn(response.body(), client)
            }
        }

        @Throws(IOException::class, CancellationException::class)
        public suspend fun silentLogin(): LoggedIn? {
            val response = client.get(RefreshToken)
            return if (response.status == HttpStatusCode.BadRequest || response.status == HttpStatusCode.Unauthorized) {
                null
            } else {
                LoggedIn(response.body(), client)
            }
        }
    }

    public class LoggedIn(private var token: Token, private val client: HttpClient) : API {
        @Throws(IOException::class, CancellationException::class)
        private suspend fun HttpRequestBuilder.addToken() {
            if (Clock.System.now() > token.payload.expiredAt) {
                token = client.get(RefreshToken).body()
            }
            bearerAuth(token.content)
        }

        @Throws(IOException::class, CancellationException::class)
        public suspend fun logout() {
            client.delete(RefreshToken) {
                addToken()
            }
        }

        @Throws(IOException::class, CancellationException::class)
        public suspend fun getMe(): User = client.get(Me) {
            addToken()
        }.body()

        @Throws(IOException::class, CancellationException::class)
        public suspend fun updateMe(user: User): User = client.put(Me) {
            contentType(ContentType.Application.Json)
            setBody(user)
            addToken()
        }.body()

        @Throws(IOException::class, CancellationException::class)
        public suspend fun deleteMe() {
            client.delete(Me) {
                addToken()
            }
        }

        @Throws(IOException::class, CancellationException::class)
        public suspend fun getTodos(): List<TodoDTO> = client.get(Todos) {
            addToken()
        }.body()

        @Throws(IOException::class, CancellationException::class)
        public suspend fun getTodo(todoID: TodoDTO.ID): TodoDTO = client.get(Todos.Id(id = todoID)) {
            addToken()
        }.body()

        @Throws(IOException::class, CancellationException::class)
        public suspend fun createTodo(todo: TodoDTO): TodoDTO = client.post(Todos) {
            contentType(ContentType.Application.Json)
            setBody(todo)
            addToken()
        }.body()

        @Throws(IOException::class, CancellationException::class)
        public suspend fun updateTodo(todoID: TodoDTO.ID, todo: TodoDTO): TodoDTO =
            client.put(Todos.Id(id = todoID)) {
                contentType(ContentType.Application.Json)
                setBody(todo)
                addToken()
            }.body()

        @Throws(IOException::class, CancellationException::class)
        public suspend fun deleteTodo(todoID: TodoDTO.ID) {
            client.delete(Todos.Id(id = todoID)) {
                addToken()
            }
        }
    }
}
