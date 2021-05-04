package app.softwork.composetodo.dto

import kotlinx.datetime.*
import kotlinx.serialization.*
import kotlinx.uuid.*

@Serializable
data class Todo(
    val id: UUID,
    val title: String,
    val until: LocalDateTime?,
    val finished: Boolean
)
