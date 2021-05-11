package app.softwork.composetodo

import app.softwork.composetodo.controller.AdminController
import app.softwork.composetodo.dto.Todo
import app.softwork.composetodo.dto.User
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.uuid.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@ExperimentalTime
internal class TodoModuleKtTest {
    private val jwt = JWTProvider(Algorithm.HMAC256("secret"), "test.com", "test", 45.seconds)

    @Test
    fun newUser() = testApplication({ db ->
        TodoModule(db, jwt)
    }) {
        assertTrue(AdminController.allUsers().isEmpty())

        val newUser: User
        register(User.New("user", "password", "password", "John", "Doe")) {
            newUser = getMe()
            with(newUser) {
                assertEquals("John", firstName)
                assertEquals("Doe", lastName)
            }
            assertEquals(listOf(newUser), AdminController.allUsers())
        }
        login("user", "password") {
            val todo = createTodo(Todo(id = UUID(), title = "New Todo", until = null, finished = false))
            assertEquals(1, getTodos().size)
            val updatedTodo = updateTodo(todo.id, todo.copy(title = "Updated Todo"))
            deleteTodo(updatedTodo.id)
            assertEquals(0, getTodos().size)
        }
    }

    @Test
    fun online() = testApplication({
        TodoModule(it, jwt)
    }) {
        with(get<HttpResponse>("/")) {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("API is online", readText())
        }
    }
}
