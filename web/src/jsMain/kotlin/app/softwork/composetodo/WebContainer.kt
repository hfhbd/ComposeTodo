package app.softwork.composetodo

import app.softwork.composetodo.repository.*
import app.softwork.composetodo.viewmodels.*
import com.squareup.sqldelight.db.*
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class WebContainer(override val scope: CoroutineScope, driver: SqlDriver) : AppContainer {
    private val db = TodoRepository.createDatabase(driver)
    override fun loginViewModel(api: API.LoggedOut) = LoginViewModel(scope, api = api) {
        this.api.value = it
    }

    override fun registerViewModel(api: API.LoggedOut) = RegisterViewModel(scope, api) {
        this.api.value = it
    }

    override fun todoViewModel(api: API.LoggedIn) = TodoViewModel(scope, TodoRepository(api = api, db.schemaQueries))

    override val client = HttpClient(Js) {
        install(HttpCookies)
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.todo.softwork.app"
            }
        }
    }
    override val api: MutableStateFlow<API> = MutableStateFlow(API.LoggedOut(client))
}
