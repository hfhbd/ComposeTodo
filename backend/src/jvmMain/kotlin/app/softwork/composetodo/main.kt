package app.softwork.composetodo

import app.softwork.ratelimit.*
import io.ktor.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.*
import kotlin.time.*

@ExperimentalTime
fun main() {
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    embeddedServer(CIO) {
        install(RateLimit)
        TodoModule()
    }.start(wait = true)
}
