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
    Navbar(api) {
        api = API.LoggedOut(client)
    }

    Main {
        Container {
            when (val currentApi = api) {
                is API.LoggedIn -> {
                    MainContent(currentApi)
                }
                is API.LoggedOut -> {
                    Text("This application uses a cold Google Cloud Run server, which usually takes 2 seconds to start.")
                    Login(currentApi) {
                        api = it
                    }
                    Register(currentApi) {
                        api = it
                    }
                }
            }
        }
    }
}

@ExperimentalTime
@Composable
fun MainContent(api: API.LoggedIn) {
    HashRouter("/todos") {
        route("/users") {
            noMatch {
                Users(api)
            }
        }
        route("/todos") {
            uuid { todoID ->
                Todo(api, todoID)
            }
            noMatch {
                Todos(TodosViewModel(api))
            }
        }
        noMatch {
            Todos(TodosViewModel(api))
        }
    }
}
