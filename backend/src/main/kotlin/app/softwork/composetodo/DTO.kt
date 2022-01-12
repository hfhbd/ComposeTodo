package app.softwork.composetodo

import app.softwork.cloudkitclient.values.*
import app.softwork.composetodo.dao.*
import kotlinx.uuid.*

fun Todo.toDTO() = app.softwork.composetodo.dto.TodoDTO(
    id = recordName.toUUID(),
    title = fields.title.value,
    until = fields.until?.value,
    finished = fields.finished.value.toBoolean(),
    recordChangeTag = recordChangeTag
)

fun app.softwork.composetodo.dto.TodoDTO.toDAO(user: User) = Todo(
    recordName = id.toString(),
    fields = Todo.Fields(
        title = Value.String(title),
        finished = Value.String(finished.toString()),
        until = until?.let { Value.DateTime(it) },
        user = Value.Reference(user)
    ),
    recordChangeTag = recordChangeTag
)

fun User.toDTO() = app.softwork.composetodo.dto.User(
    username = recordName,
    firstName = fields.firstName.value,
    lastName = fields.lastName.value,
    recordChangeTag = recordChangeTag!!
)

fun app.softwork.composetodo.dto.User.New.toDAO() = User(
    recordName = username,

    fields = User.Fields(
        firstName = Value.String(firstName),
        lastName = Value.String(lastName),
        password = Value.String(password)
    ),
    recordChangeTag = null
)

fun app.softwork.composetodo.dto.User.toDAO() = User(
    recordName = username,
    fields = User.Fields(
        firstName = Value.String(firstName),
        lastName = Value.String(lastName),
        password = null
    ),
    recordChangeTag = recordChangeTag
)
