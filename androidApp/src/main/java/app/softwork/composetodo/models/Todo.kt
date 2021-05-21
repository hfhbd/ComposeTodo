package app.softwork.composetodo.models

import androidx.room.*
import kotlinx.datetime.*
import kotlinx.uuid.*
import app.softwork.composetodo.dto.Todo as RestTodo

@Entity
data class Todo(
    @PrimaryKey val id: UUID,
    val title: String,
    val until: Instant?,
    val finished: Boolean,
    val recordChangeTag: String?
) {
    constructor(todo: RestTodo) : this(
        id = todo.id,
        title = todo.title,
        until = todo.until,
        finished = todo.finished,
        recordChangeTag = todo.recordChangeTag
    )

    fun toDTO() = RestTodo(
        id = id,
        title = title,
        until = until,
        finished = finished, recordChangeTag = recordChangeTag)
}
