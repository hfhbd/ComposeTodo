package app.softwork.composetodo

import app.softwork.cloudkitclient.*
import app.softwork.ratelimit.*
import com.auth0.jwt.algorithms.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.slf4j.*
import java.util.*
import kotlin.reflect.*
import kotlin.time.*

@ExperimentalTime
fun main() {
    val db = client().publicDB
    val jwtProvider = JWTProvider(
        Algorithm.HMAC512("secret"),
        "app.softwork.todo",
        "app.softwork.todo",
        expireDuration = 10.minutes
    )

    embeddedServer(CIO) {
        install(CORS) {
            host("todo.softwork.app", listOf("https"))
            host("localhost:8080")
            allowCredentials = true
            header(HttpHeaders.Authorization)
            header(HttpHeaders.ContentType)
            method(HttpMethod.Delete)
            method(HttpMethod.Put)
        }
        install(RateLimit) {
            skip { call ->
                if(call.request.local.uri == "/login") {
                    SkipResult.ExecuteRateLimit
                } else {
                    SkipResult.SkipRateLimit
                }
            }
        }
        TodoModule(db = db, jwtProvider = jwtProvider)
    }.start(wait = true)
}

private fun client(): CKClient {
    val container = "iCloud.app.softwork.composetodo"
    val keyID by Env
    val privateKey by Env
    val logger = LoggerFactory.getLogger(CKClient::class.java)
    return CKClient(container, keyID, Base64.getMimeDecoder().decode(privateKey), logging = {
        logger.debug(it)
    })
}

object Env {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        System.getenv(property.name) ?: error("${property.name} not passed as environment")
}
