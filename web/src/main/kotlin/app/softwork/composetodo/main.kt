package app.softwork.composetodo

import app.cash.sqldelight.driver.sqljs.*
import app.softwork.composetodo.repository.*
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import org.jetbrains.compose.web.*

suspend fun main() {
    // https://youtrack.jetbrains.com/issue/KTOR-539
    js(
        """
window.originalFetch = window.fetch;
window.fetch = function (resource, init) {
    return window.originalFetch(resource, Object.assign({ credentials: 'include' }, init || {}));
};
"""
    )
    val client = HttpClient(Js) {
        install(HttpCookies)
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

    val driver = initSqlDriver(ComposeTodoDB.Schema).await()
    val db = TodoRepository.createDatabase(driver)
    val appContainer = AppContainer(client, db)

    renderComposable(rootElementId = "root") {
        MainApp(appContainer)
    }
}
