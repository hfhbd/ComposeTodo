package app.softwork.composetodo

import androidx.compose.ui.window.*
import app.cash.sqldelight.db.*
import app.cash.sqldelight.driver.jdbc.sqlite.*
import app.softwork.composetodo.repository.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

fun main() {
    val client = HttpClient(CIO) {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.todo.softwork.app"
            }
        }
        install(Resources)
        install(ContentNegotiation) {
            json()
        }
    }
    val driver = JdbcSqliteDriver("jdbc:sqlite:composetodo.db")
    ComposeTodoDB.Schema.migrate(driver, 0, 1)

    val appContainer = AppContainer(client, TodoRepository.createDatabase(driver))

    singleWindowApplication {
        MainView(appContainer)
    }
}
