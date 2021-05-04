package app.softwork.composetodo

import app.softwork.composetodo.controller.*
import app.softwork.composetodo.dto.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import com.auth0.jwt.algorithms.*
import io.ktor.http.*
import kotlin.test.*
import kotlin.time.*

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
