package app.softwork.composetodo.models

import kotlinx.datetime.LocalDateTime
import kotlinx.uuid.UUID

data class TodoEntity(
    override val id: UUID,
    override val title: String,
    override val until: LocalDateTime,
    override val finished: Boolean
) : Todo {
    constructor(todo: app.softwork.composetodo.dto.Todo) : this(
        todo.id,
        todo.title,
        todo.until,
        todo.finished
    )

    fun toDTO() = app.softwork.composetodo.dto.Todo(id, title, until, finished)
}
