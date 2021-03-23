package app.softwork.composetodo

import androidx.compose.desktop.Window
import app.softwork.composetodo.models.*
import app.softwork.composetodo.repository.TodoRepo
import app.softwork.composetodo.viewmodels.LoginViewModel
import app.softwork.composetodo.viewmodels.PressMeViewModel
import app.softwork.composetodo.viewmodels.TodoViewModel
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

fun main() {
    val api = API(HttpClient(CIO))
    val scope = MainScope()
    val appContainer = object : AppContainer<TodoEntity> {
        override val todoViewModel = TodoViewModel(scope, TodoRepo(api))
        override val pressMeViewModel = PressMeViewModel(scope)
        override val loginViewModel = LoginViewModel(scope, api)
    }

    Window(onDismissRequest = {
        scope.cancel()
    }) {
        MainView(appContainer)
    }
}
