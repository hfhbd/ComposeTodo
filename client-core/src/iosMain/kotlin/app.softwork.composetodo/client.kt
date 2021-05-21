package app.softwork.composetodo

import io.ktor.client.*
import io.ktor.client.engine.ios.*
import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.http.*

fun api(cookiesStorage: CookiesStorage) = API.LoggedOut(HttpClient(Ios) {
    install(HttpCookies) {
        storage = cookiesStorage
    }
    install(DefaultRequest) {
        url {
            protocol = URLProtocol.HTTP
            host = "localhost"
        }
    }
})