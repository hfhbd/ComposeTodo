package com.example.composetodo

import com.example.composetodo.dao.Todo
import com.example.composetodo.dao.User
import kotlinx.datetime.toKotlinLocalDateTime

fun Todo.toDTO() = com.example.composetodo.dto.Todo(
    id = id.value,
    title = title,
    until = until.toKotlinLocalDateTime(),
    finished = finished
)

fun User.toDTO() = com.example.composetodo.dto.User(
    id = id.value, firstName = firstName, lastName = lastName
)