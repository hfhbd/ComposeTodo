package app.softwork.composetodo

import app.softwork.composetodo.repository.*
import app.softwork.composetodo.viewmodels.*
import com.squareup.sqldelight.db.*
import com.squareup.sqldelight.drivers.native.*
import io.ktor.client.*
import io.ktor.client.engine.ios.*
import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.client.features.logging.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class IosContainer(
    override val scope: CoroutineScope,
    protocol: URLProtocol,
    host: String
) : AppContainer {
    private val db = TodoRepository.createDatabase(NativeSqliteDriver(ComposeTodoDB.Schema, "composetodo.db"))

    constructor() : this(scope = MainScope(), protocol = URLProtocol.HTTPS, host = "api.todo.softwork.app")

    override val client: HttpClient = HttpClient(Ios) {
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
        engine {
            configureRequest {
                setAllowsCellularAccess(true)
            }
        }
    }

    override fun loginViewModel(api: API.LoggedOut) = LoginViewModel(scope, api) {
        this.api.value = it
    }

    override fun todoViewModel(api: API.LoggedIn) =
        TodoViewModel(scope = scope, repo = TodoRepository(api, db.schemaQueries))

    override fun registerViewModel(api: API.LoggedOut) = RegisterViewModel(scope, api) {
        this.api.value = it
    }

    override val api: MutableStateFlow<API> = MutableStateFlow(API.LoggedOut(client))
}
