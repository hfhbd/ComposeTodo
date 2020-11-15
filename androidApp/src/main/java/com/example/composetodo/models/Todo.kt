package com.example.composetodo.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.composetodo.todo.Todo as RestTodo

@Entity
data class Todo(
    @PrimaryKey val id: Int,
    val title: String,
    val completed: Boolean
) {
    constructor(todo: RestTodo): this(todo.id, todo.title, todo.completed)
    val dto get() = RestTodo(id, -1, title, completed)
}
