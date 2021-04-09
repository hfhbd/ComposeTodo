package app.softwork.composetodo.definitions

import kotlinx.uuid.exposed.KotlinxUUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object Todos: KotlinxUUIDTable() {
    val user = reference("userID", Users)
    val title = varchar("title", 512)
    val until = datetime("until").nullable()
    val finished = bool("finished")
}