package app.softwork.composetodo.models

import kotlinx.datetime.LocalDateTime
import kotlinx.uuid.UUID

interface Todo {
    val id: UUID
    val title: String
    val until: LocalDateTime
    val finished: Boolean

    companion object
}
