package app.softwork.composetodo

import app.softwork.cloudkitclient.*
import com.auth0.jwt.algorithms.*
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.cors.*
import org.slf4j.*
import java.util.*
import kotlin.reflect.*
import kotlin.time.Duration.Companion.minutes

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
            allowHost("todo.softwork.app", listOf("https"))
            allowHost("localhost:8080")
            allowCredentials = true
            allowHeader(HttpHeaders.Authorization)
            allowHeader(HttpHeaders.ContentType)
            allowMethod(HttpMethod.Delete)
            allowMethod(HttpMethod.Put)
        }
        TodoModule(db = db, jwtProvider = jwtProvider)
    }.start(wait = true)
}

private fun client(): Client {
    val container = "iCloud.app.softwork.composetodo"
    val keyID = System.getenv("keyID") ?: return TestClient()

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
