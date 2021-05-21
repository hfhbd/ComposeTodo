package app.softwork.composetodo

import androidx.compose.desktop.*
import app.softwork.composetodo.models.*
import app.softwork.composetodo.repository.*
import app.softwork.composetodo.viewmodels.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.http.*
import kotlinx.coroutines.*

fun main() {
    val api = API.LoggedOut(HttpClient(CIO) {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.todo.softwork.app"
            }
        }
    })
    val scope = MainScope()
    val appContainer = object : AppContainer<TodoEntity> {
        override fun todoViewModel(api: API.LoggedIn) = TodoViewModel(scope, TodoRepo(api))
        override val pressMeViewModel = PressMeViewModel(scope)
        override val loginViewModel = LoginViewModel(scope, api)
    }

    Window(onDismissRequest = {
        scope.cancel()
    }) {
        MainView(appContainer)
    }
}
