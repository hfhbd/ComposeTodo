package com.example.composetodo

import com.example.composetodo.dao.Todo
import com.example.composetodo.dao.User
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.uuid.toKotlinUUID

fun Todo.toDTO() = com.example.composetodo.dto.Todo(
    id = id.value.toKotlinUUID(),
    title = title,
    until = until.toKotlinLocalDateTime(),
    finished = finished
)

fun User.toDTO() = com.example.composetodo.dto.User(
    id = id.value.toKotlinUUID(), firstName = firstName, lastName = lastName
)