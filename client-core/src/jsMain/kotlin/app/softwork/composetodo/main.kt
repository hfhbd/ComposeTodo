package app.softwork.composetodo

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.http.*
import kotlinx.browser.*
import kotlinx.coroutines.*
import react.dom.*

val api = API(HttpClient(Js) {
    install(HttpCookies)
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.todo.softwork.app"
        }
    }
})

val scope = MainScope()

fun main() {
    render(document.getElementById("root")) {
        mainApp()
    }
}
