package app.softwork.composetodo.dto

import kotlinx.serialization.*
import kotlinx.uuid.*

// id is a function, otherwise Ktor conversion service fails when serializing kotlinx.uuid.UUID
@Serializable
public data class RefreshToken(val value: String) {
    public fun id(): UUID = UUID(value)
}
