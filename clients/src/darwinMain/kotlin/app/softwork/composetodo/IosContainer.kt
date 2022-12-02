package app.softwork.composetodo

import app.cash.sqldelight.driver.native.*
import app.softwork.composetodo.repository.*
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import io.ktor.http.URLProtocol.Companion.HTTPS
import io.ktor.serialization.kotlinx.json.*

fun IosContainer(
    storage: CookiesStorage
) = IosContainer(
    HTTPS,
    "api.todo.softwork.app",
    storage
)

fun IosContainer(
    protocol: URLProtocol,
    host: String,
    storage: CookiesStorage
): AppContainer {
    val client = HttpClient(Darwin) {
        install(HttpCookies) {
            this.storage = storage
        }
        install(DefaultRequest) {
            url {
                this.protocol = protocol
                this.host = host
            }
        }
        install(Logging) {
            level = LogLevel.ALL
        }
        install(Resources)
        install(ContentNegotiation) {
            json()
        }
    }
    val db = TodoRepository.createDatabase(NativeSqliteDriver(ComposeTodoDB.Schema, "composetodo.db"))

    return AppContainer(client, db)
}
