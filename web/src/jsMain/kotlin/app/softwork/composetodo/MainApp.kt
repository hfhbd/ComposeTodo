package app.softwork.composetodo

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.login.*
import app.softwork.composetodo.routing.*
import app.softwork.composetodo.todos.*
import app.softwork.composetodo.users.*
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.http.*
import org.jetbrains.compose.web.dom.*

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

@Composable
fun MainContent(api: API.LoggedIn) {
    val path by path(42)
    when (path) {
        "/users" -> {
            Users(api)
        }
        else -> {
            Todos(TodosViewModel(api))
        }
    }
}