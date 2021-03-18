package app.softwork.composetodo.models

import androidx.room.*
import kotlinx.datetime.*
import kotlinx.uuid.*
import app.softwork.composetodo.dto.Todo as RestTodo

@Entity(tableName = "todo")
data class TodoEntity(
    @PrimaryKey override val id: UUID,
    override val title: String,
    override val until: Instant?,
    override val finished: Boolean,
    val recordChangeTag: String?
): Todo {
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
