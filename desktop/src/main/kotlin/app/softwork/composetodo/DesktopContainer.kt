package app.softwork.composetodo

import app.softwork.composetodo.repository.*
import app.softwork.composetodo.repository.TodoRepository.Companion.createDatabase
import app.softwork.composetodo.viewmodels.*
import com.squareup.sqldelight.db.*
import com.squareup.sqldelight.sqlite.driver.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.flow.*

class DesktopContainer : AppContainer {
    private val db: ComposeTodoDB
    init {
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:composetodo.db")
        ComposeTodoDB.Schema.migrate(driver, 0, 1)
        db = createDatabase(driver)
    }
    override fun todoViewModel(api: API.LoggedIn): TodoViewModel =
        TodoViewModel(TodoRepository(api, db.todoQueries))

    override fun loginViewModel(api: API.LoggedOut) = LoginViewModel(api) {
        this.api.value = it
    }

    override fun registerViewModel(api: API.LoggedOut) = RegisterViewModel(api) {
        this.api.value = it
    }

    override val client = HttpClient(CIO) {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.todo.softwork.app"
            }
        }
    }
    override val api: MutableStateFlow<API> = MutableStateFlow(API.LoggedOut(client))
}
