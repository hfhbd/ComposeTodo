package app.softwork.composetodo

import app.softwork.composetodo.controller.*
import app.softwork.composetodo.dto.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.uuid.*
import kotlin.test.*

internal class TodoModuleKtTest {
    @Test
    fun newUser() = testApplication({ db ->
        TodoModule(db, TODO())
    }) { db ->
        assertTrue(AdminController(db).allUsers().isEmpty())

        register(User.New("user", "password", "password", "John", "Doe")) {
            val newUser = getMe()
            with(newUser) {
                assertEquals("John", firstName)
                assertEquals("Doe", lastName)
            }
            assertEquals(listOf(newUser), AdminController(db).allUsers())
        }
        login("user", "password") {
            val todo = createTodo(
                TodoDTO(
                    id = TodoDTO.ID(UUID()),
                    title = "New Todo",
                    until = null,
                    finished = false,
                    recordChangeTag = null
                )
            )
            assertEquals(1, getTodos().size)
            val updatedTodo = updateTodo(todo.id, todo.copy(title = "Updated Todo"))
            deleteTodo(updatedTodo.id)
            assertEquals(0, getTodos().size)
        }
    }

    @Test
    fun online() = testApplication({
        TodoModule(it, TODO())
    }) {
        with(get("/")) {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("API is online", bodyAsText())
        }
    }
}
