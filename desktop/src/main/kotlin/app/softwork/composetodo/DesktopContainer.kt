package app.softwork.composetodo

import app.softwork.composetodo.repository.*
import app.softwork.composetodo.viewmodels.*
import com.squareup.sqldelight.db.*
import com.squareup.sqldelight.sqlite.driver.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class DesktopContainer(private val scope: CoroutineScope) : AppContainer {
    override val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:composetodo.db")
    override fun todoViewModel(api: API.LoggedIn): TodoViewModel = TodoViewModel(scope, TodoRepository(api, driver))

    override fun loginViewModel(api: API.LoggedOut) = LoginViewModel(scope, api) {
        isLoggedIn.value = it
    }

    override fun registerViewModel(api: API.LoggedOut) = RegisterViewModel(scope, api) {
        isLoggedIn.value = it
    }

    override val client = HttpClient(CIO) {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTP
                host = "localhost"
            }
        }
    }
    override val isLoggedIn: MutableStateFlow<API> = MutableStateFlow(API.LoggedOut(client))
}
