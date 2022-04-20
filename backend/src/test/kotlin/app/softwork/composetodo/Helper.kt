package app.softwork.composetodo

import app.softwork.cloudkitclient.*
import app.softwork.composetodo.dto.*
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import kotlin.contracts.*
import kotlin.test.*

suspend fun dbTest(
    test: suspend (Client.Database) -> Unit
) {
    val db = TestClient().publicDB
    test(db)
}

fun testApplication(
    setup: Application.(Client.Database) -> Unit,
    tests: suspend HttpClient.(Client.Database) -> Unit
) {
    testApplication {
        dbTest { db ->
            application {
                setup(db)
            }
            val client = createClient {
                defaultRequest {
                    url {
                        protocol = URLProtocol.HTTPS
                    }
                }
                install(Resources)
                install(ContentNegotiation) {
                    json()
                }
            }
            client.tests(db)
        }
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
    val api = API.LoggedOut(this).register(user)
    assertNotNull(api)
    result = api.block()
    api.logout()
    return result
}

@OptIn(ExperimentalContracts::class)
suspend fun <T> HttpClient.login(
    username: String,
    passwort: String,
    block: suspend API.LoggedIn.() -> T
): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    val result: T
    val api = assertNotNull(API.LoggedOut(this).login(username, passwort))
    result = api.block()
    api.logout()
    return result
}
