package app.softwork.composetodo

import app.softwork.ratelimit.*
import com.auth0.jwt.algorithms.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.*
import kotlin.time.*

@ExperimentalTime
fun main() {
    val db = Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    val jwtProvider = JWTProvider(
        Algorithm.HMAC512("secret"),
        "app.softwork.composetodo",
        "app.softwork.composetodo",
        expireDuration = 10.minutes
    )

    embeddedServer(CIO) {
        install(RateLimit)
        install(CORS) {
            host("composetodo.softwork.app", listOf("https"))
            host("localhost:8080")
            allowCredentials = true
            header(HttpHeaders.Authorization)
            header(HttpHeaders.ContentType)
            method(HttpMethod.Delete)
            method(HttpMethod.Put)
        }
        TodoModule(db = db, jwtProvider = jwtProvider)
    }.start(wait = true)
}
