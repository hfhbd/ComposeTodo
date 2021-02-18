package app.softwork.composetodo

import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database

fun main() {
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    embeddedServer(CIO) {
        TodoModule()
    }.start(wait = true)
}
