package app.softwork.composetodo

import app.softwork.cloudkitclient.*
import app.softwork.composetodo.dto.*
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.server.testing.client.*
import kotlinx.coroutines.*
import kotlin.contracts.*
import kotlin.test.*

fun dbTest(
    test: suspend (Client.Database) -> Unit
) {
    val db = TestClient().publicDB
    runBlocking {
        test(db)
    }
}

@OptIn(ExperimentalContracts::class)
public fun testApplication(setup: Application.(Client.Database) -> Unit, tests: suspend HttpClient.(Client.Database) -> Unit) {
    contract {
        callsInPlace(setup, InvocationKind.EXACTLY_ONCE)
        callsInPlace(tests, InvocationKind.EXACTLY_ONCE)
    }
    withTestApplication {
        dbTest { db ->
            application.setup(db)
            client.tests(db)
        }
    }
}

@OptIn(ExperimentalContracts::class)
private fun HttpClient.cookiesSession(callback: suspend () -> Unit) {
    contract {
        callsInPlace(callback, InvocationKind.EXACTLY_ONCE)
    }
    val app = (engineConfig as TestHttpClientConfig).app
    val trackedCookies: MutableList<Cookie> = mutableListOf()

    app.hookRequests(
        processRequest = { setup ->
            addHeader(
                HttpHeaders.Cookie,
                trackedCookies.joinToString("; ") {
                    (it.name).encodeURLParameter() + "=" + (it.value).encodeURLParameter()
                }
            )
            setup() // setup after setting the cookie so the user can override cookies
        },
        processResponse = {
            trackedCookies += response.headers.values(HttpHeaders.SetCookie).map { parseServerSetCookieHeader(it) }
        }
    ) {
        runBlocking { callback() }
    }
}

@OptIn(ExperimentalContracts::class)
suspend fun <T> HttpClient.register(
    user: User.New,
    block: suspend API.LoggedIn.() -> T
): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    val result: T
    cookiesSession {
        val api = API.LoggedOut(this).register(user)
        assertNotNull(api)
        result = api.block()
        api.logout()
    }
    return result
}

@OptIn(ExperimentalContracts::class)
suspend fun <T> HttpClient.login(
    username: String, passwort: String,
    block: suspend API.LoggedIn.() -> T
): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    val result: T
    cookiesSession {
        val api = assertNotNull(API.LoggedOut(this).login(username, passwort))
        result = api.block()
        api.logout()
    }
    return result
}
