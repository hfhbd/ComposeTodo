package app.softwork.composetodo.dto

import kotlinx.serialization.*
import kotlinx.uuid.*

@Serializable
data class RefreshToken(val value: String) {
    fun id() = UUID(value)
}
