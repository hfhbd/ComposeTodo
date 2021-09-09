package app.softwork.composetodo.models

import kotlinx.datetime.*
import kotlinx.uuid.*

data class TodoEntity(
    override val id: UUID,
    override val title: String,
    override val until: Instant?,
    override val finished: Boolean,
    override val recordChangeTag: String?
) : Todo {
    constructor(todo: app.softwork.composetodo.dto.Todo) : this(
        id = todo.id,
        title = todo.title,
        until = todo.until,
        finished = todo.finished,
        recordChangeTag = todo.recordChangeTag
    )

    fun toDTO() = app.softwork.composetodo.dto.Todo(
        id = id,
        title = title,
        until = until,
        finished = finished,
        recordChangeTag = recordChangeTag
    )
}
