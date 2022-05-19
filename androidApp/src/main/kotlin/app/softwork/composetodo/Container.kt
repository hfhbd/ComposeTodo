package app.softwork.composetodo

import android.content.*
import app.softwork.composetodo.repository.*
import app.softwork.composetodo.repository.TodoRepository.Companion.createDatabase
import app.softwork.composetodo.viewmodels.*
import com.squareup.sqldelight.android.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.*

class Container(applicationContext: Context) : AppContainer {
    private val db = createDatabase(AndroidSqliteDriver(ComposeTodoDB.Schema, applicationContext, "composetodo.db"))

    override val client = HttpClient(Android) {
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

    override val api: MutableStateFlow<API> = MutableStateFlow(API.LoggedOut(client))

    override fun loginViewModel(api: API.LoggedOut) = LoginViewModel(api = api) {
        this.api.value = it
    }

    override fun registerViewModel(api: API.LoggedOut) = RegisterViewModel(api) {
        this.api.value = it
    }

    override fun todoViewModel(api: API.LoggedIn) =
        TodoViewModel(TodoRepository(api = api, dao = db.todoQueries))
}
