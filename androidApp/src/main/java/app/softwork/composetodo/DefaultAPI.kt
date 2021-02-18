package app.softwork.composetodo

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.http.*

val api = API(HttpClient(Android) {
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.todo.softwork.app"
        }
    }
})
