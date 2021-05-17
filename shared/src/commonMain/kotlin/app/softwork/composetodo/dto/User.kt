package app.softwork.composetodo.dto

import kotlinx.serialization.*

@Serializable
data class User(
    val username: String,
    val firstName: String,
    val lastName: String,
    val recordChangeTag: String
) {
    @Serializable
    data class New(
        val username: String,
        val password: String,
        val passwordAgain: String,
        val firstName: String,
        val lastName: String
    )
}
