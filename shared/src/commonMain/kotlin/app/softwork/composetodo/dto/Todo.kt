package app.softwork.composetodo.dto

import app.softwork.composetodo.dto.serializer.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.uuid.UUID

@Serializable
data class Todo(
    val id: UUID,
    val title: String,
    @Serializable(with = LocalDateTimeSerializer::class) val until: LocalDateTime?,
    val finished: Boolean
)