package app.softwork.composetodo.dto

import kotlinx.serialization.*
import kotlinx.uuid.*

@Serializable
data class User(val id: UUID, val firstName: String, val lastName: String) {
    @Serializable
    data class New(
        val username: String,
        val password: String,
        val passwordAgain: String,
        val firstName: String,
        val lastName: String
    )
}
