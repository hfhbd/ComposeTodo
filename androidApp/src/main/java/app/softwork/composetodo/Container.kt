package app.softwork.composetodo

import android.content.*
import app.softwork.composetodo.repository.*
import app.softwork.composetodo.repository.TodoRepository.Companion.createDatabase
import app.softwork.composetodo.viewmodels.*
import com.squareup.sqldelight.android.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class Container(applicationContext: Context, override val scope: CoroutineScope) : AppContainer {
    private val db = createDatabase(AndroidSqliteDriver(ComposeTodoDB.Schema, applicationContext, "composetodo.db"))

    override val client = HttpClient(Android) {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.todo.softwork.app"
            }
        }
    }

    override val api: MutableStateFlow<API> = MutableStateFlow(API.LoggedOut(client))

    override fun loginViewModel(api: API.LoggedOut) = LoginViewModel(scope, api = api) {
        this.api.value = it
    }

    override fun registerViewModel(api: API.LoggedOut) = RegisterViewModel(scope, api) {
        this.api.value = it
    }

    override fun todoViewModel(api: API.LoggedIn) =
        TodoViewModel(scope, TodoRepository(api = api, dao = db.todoQueries))
}
