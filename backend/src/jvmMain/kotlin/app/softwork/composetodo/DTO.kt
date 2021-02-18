package app.softwork.composetodo

import app.softwork.composetodo.dao.Todo
import app.softwork.composetodo.dao.User
import kotlinx.datetime.toKotlinLocalDateTime

fun Todo.toDTO() = app.softwork.composetodo.dto.Todo(
    id = id.value,
    title = title,
    until = until.toKotlinLocalDateTime(),
    finished = finished
)

fun User.toDTO() = app.softwork.composetodo.dto.User(
    id = id.value, firstName = firstName, lastName = lastName
)