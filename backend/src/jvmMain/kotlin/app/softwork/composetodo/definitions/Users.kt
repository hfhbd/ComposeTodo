package app.softwork.composetodo.definitions

import kotlinx.uuid.exposed.*

object Users: KotlinxUUIDTable() {
    val name = varchar("name", length = 64).uniqueIndex()
    val password = varchar("password", 128)

    val firstName = varchar("firstName", 128)
    val lastName = varchar("lastName", 128)
}
