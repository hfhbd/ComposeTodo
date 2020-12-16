package com.example.composetodo.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlinx.uuid.UUID
import com.example.composetodo.dto.Todo as RestTodo

@Entity
data class Todo(
    @PrimaryKey val id: UUID,
    val title: String,
    val until: LocalDateTime,
    val finished: Boolean
) {
    constructor(todo: RestTodo) : this(
        todo.id,
        todo.title,
        todo.until,
        todo.finished
    )

    fun toDTO() = RestTodo(id, title, until, finished)
}
