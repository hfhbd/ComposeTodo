package app.softwork.composetodo.dto

import kotlinx.serialization.*
import kotlinx.uuid.*

// id is a function, otherwise Ktor conversion service fails when serializing kotlinx.uuid.UUID
@Serializable
data class RefreshToken(val value: String) {
    fun id() = UUID(value)
}
