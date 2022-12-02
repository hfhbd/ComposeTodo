package app.softwork.composetodo

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.dto.*
import app.softwork.composetodo.login.*
import app.softwork.composetodo.todos.*
import app.softwork.composetodo.users.*
import app.softwork.routingcompose.*
import org.jetbrains.compose.web.dom.*

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
                LoginView(appContainer)
            }
        }
    }
}

@Composable
private fun LoginView(appContainer: AppContainer) {
    Content(emptyList(), onLogout = null) {
        Text("This application uses a cold Google Cloud Run server, which usually takes 2 seconds to start.")
        Login(appContainer.loginViewModel())
        Register(appContainer.registerViewModel())
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
}

@Composable
private fun RouteBuilder.MainContent(appContainer: AppContainer, api: API.LoggedIn) {
    val links = listOf("To-Dos" to "/todos", "Users" to "/users")

    Content(links, {
        appContainer.logout()
    }) {
        route("users") {
            Users()
        }
        route("todos") {
            uuid { todoID ->
                Todo(api, TodoDTO.ID(todoID))
            }
            noMatch {
                Todos(appContainer.todoViewModel())
            }
        }
        noMatch {
            redirect("todos", true)
        }
    }
}
