package app.softwork.composetodo

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.login.*
import app.softwork.composetodo.todos.*
import app.softwork.composetodo.users.*
import app.softwork.routingcompose.*
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.http.*
import org.jetbrains.compose.web.dom.*
import kotlin.time.*

@ExperimentalTime
@Composable
fun MainApp() {
    val client = HttpClient(Js) {
        install(HttpCookies)
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.todo.softwork.app"
            }
        }
    }
    var api: API by remember { mutableStateOf(API.LoggedOut(client)) }
    LaunchedEffect(Unit) {
        (api as? API.LoggedOut)?.let {
            it.silentLogin()?.let { api = it }
        }
    }

    when (val currentApi = api) {
        is API.LoggedIn -> {
            MainContent(currentApi) {
                api = API.LoggedOut(client)
            }
        }
        is API.LoggedOut -> {
            LoginView(currentApi) {
                api = it
            }
        }
    }
}

@Composable
private fun LoginView(api: API.LoggedOut, onLogin: (API.LoggedIn) -> Unit) {
    Content(emptyList(), api, onLogout = {}) {

        Text("This application uses a cold Google Cloud Run server, which usually takes 2 seconds to start.")
        Login(api, onLogin)
        Register(api, onLogin)
    }
}

@Composable
private fun Content(links: List<Pair<String, String>>, api: API, onLogout: () -> Unit, content: @Composable () -> Unit) {
    Navbar(links, api, onLogout)
    Main {
        Container {
            content()
        }
    }
}

@ExperimentalTime
@Composable
private fun MainContent(api: API.LoggedIn, onLogout: () -> Unit) {
    val links = listOf("Todos" to "/todos", "Users" to "/users")

    HashRouter("/todos") {
        route("users") {
            noMatch {
                Content(links, api, onLogout) {
                    Users(api)
                }
            }
        }
        route("todos") {
            uuid { todoID ->
                Content(links, api, onLogout) {
                    Todo(api, todoID)
                }
            }
            noMatch {
                Content(links, api, onLogout) {
                    Todos(TodosViewModel(api))
                }
            }
        }
        noMatch {
            Content(links, api, onLogout) {
                Todos(TodosViewModel(api))
            }
        }
    }
}
