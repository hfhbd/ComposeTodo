package app.softwork.composetodo

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.dto.*
import app.softwork.composetodo.login.*
import app.softwork.composetodo.todos.*
import app.softwork.composetodo.users.*
import app.softwork.routingcompose.*
import kotlinx.coroutines.*
import org.jetbrains.compose.web.dom.*
import kotlin.uuid.ExperimentalUuidApi

@ExperimentalUuidApi
@Composable
fun MainApp(appContainer: AppContainer) {
    val api by appContainer.api.collectAsState()

    LaunchedEffect(Unit) {
        (api as? API.LoggedOut)?.let {
            it.silentLogin()?.let {
                appContainer.api.value = it
            }
        }
    }

    HashRouter("/") {
        when (val currentApi = api) {
            is API.LoggedIn -> {
                MainContent(appContainer, currentApi)
            }
            is API.LoggedOut -> {
                LoginView(appContainer, currentApi)
            }
        }
    }
}

@Composable
private fun LoginView(appContainer: AppContainer, api: API.LoggedOut) {
    Content(emptyList(), onLogout = null) {
        Text("This application uses a cold Google Cloud Run server, which usually takes 2 seconds to start.")
        Login(appContainer.loginViewModel(api))
        Register(appContainer.registerViewModel(api))
    }
}

@Composable
private fun Content(
    links: List<Pair<String, String>>,
    onLogout: (() -> Unit)?,
    content: @Composable () -> Unit
) {
    Navbar(links, onLogout)
    Main {
        Container {
            content()
        }
    }
    Footer(attrs = { classes("footer", "mt-auto") }) {
        Container {
            Hr()
            P {
                Text("© Softwork.app")
            }
        }
    }
}

@ExperimentalUuidApi
@Composable
private fun RouteBuilder.MainContent(appContainer: AppContainer, api: API.LoggedIn) {
    val links = listOf("To-Dos" to "/todos", "Users" to "/users")

    Content(links, {
        scope.launch {
            appContainer.logout()
        }
    }) {
        route("users") {
            Users()
        }
        route("todos") {
            uuid { todoID ->
                Todo(api, TodoDTO.ID(todoID))
            }
            noMatch {
                Todos(appContainer.todoViewModel(api))
            }
        }
        noMatch {
            redirect("todos", true)
        }
    }
}
