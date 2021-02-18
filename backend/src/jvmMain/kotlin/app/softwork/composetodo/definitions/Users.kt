package app.softwork.composetodo.definitions

import kotlinx.uuid.exposed.KotlinxUUIDTable

object Users: KotlinxUUIDTable() {
    val firstName = varchar("firstName", 128)
    val lastName = varchar("lastName", 128)
}