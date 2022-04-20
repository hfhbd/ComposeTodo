package app.softwork.composetodo

import app.softwork.composetodo.repository.*
import app.softwork.composetodo.viewmodels.*
import com.squareup.sqldelight.drivers.native.*
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.*

class IosContainer(
    protocol: URLProtocol,
    host: String
) : AppContainer {
    private val db = TodoRepository.createDatabase(NativeSqliteDriver(ComposeTodoDB.Schema, "composetodo.db"))

    constructor() : this(protocol = URLProtocol.HTTPS, host = "api.todo.softwork.app")

    override val client: HttpClient = HttpClient(Darwin) {
        install(HttpCookies) {
            storage = UserDefaultsCookieStorage()
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

    override fun loginViewModel(api: API.LoggedOut) = LoginViewModel(api) {
        this.api.value = it
    }

    override fun todoViewModel(api: API.LoggedIn) =
        TodoViewModel(repo = TodoRepository(api, db.todoQueries))

    override fun registerViewModel(api: API.LoggedOut) = RegisterViewModel(api) {
        this.api.value = it
    }

    override val api: MutableStateFlow<API> = MutableStateFlow(API.LoggedOut(client))
}
