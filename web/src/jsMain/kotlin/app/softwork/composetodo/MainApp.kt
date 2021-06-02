package app.softwork.composetodo

import androidx.compose.runtime.*
import androidx.compose.web.elements.*
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
    Navbar(api) {
        api = API.LoggedOut(client)
    }

    Main {
        Container {
            when (val currentApi = api) {
                is API.LoggedIn ->
                    when (path()) {
                        "/users" -> {
                            Users(currentApi)
                        }
                        else -> {
                            Todos(TodosViewModel(currentApi))
                        }
                    }
                is API.LoggedOut -> {
                    Text("This webpage uses a cold Google Cloud Run server, which usally takes 2 seconds to start the backend.")
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
