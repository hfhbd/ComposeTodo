package app.softwork.composetodo

import app.softwork.composetodo.dto.*
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.server.testing.client.*
import kotlinx.coroutines.*
import kotlinx.uuid.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import kotlin.contracts.*
import kotlin.test.*

@OptIn(ExperimentalContracts::class)
fun dbTest(
    name: String = "test${UUID()}",
    setup: suspend (Database) -> Unit = { },
    test: suspend (Database) -> Unit
) {
    contract {
        callsInPlace(setup, InvocationKind.EXACTLY_ONCE)
        callsInPlace(test, InvocationKind.EXACTLY_ONCE)
    }
    val db = Database.connect("jdbc:h2:mem:$name;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    transaction(db) {
        runBlocking {
            setup(db)
        }
    }
    runBlocking {
        test(db)
    }
    TransactionManager.closeAndUnregister(db)
}

@OptIn(ExperimentalContracts::class)
public fun testApplication(setup: Application.(Database) -> Unit, tests: suspend HttpClient.(Database) -> Unit) {
    contract {
        callsInPlace(setup, InvocationKind.EXACTLY_ONCE)
        callsInPlace(tests, InvocationKind.EXACTLY_ONCE)
    }
    withTestApplication {
        dbTest(setup = {
            application.setup(it)
        }) { db ->
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
