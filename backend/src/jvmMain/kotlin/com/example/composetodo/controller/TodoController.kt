package com.example.composetodo.controller

import com.example.composetodo.dao.Todo
import com.example.composetodo.dao.User
import com.example.composetodo.toDTO
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.uuid.UUID
import kotlinx.uuid.toJavaUUID
import kotlinx.uuid.toKotlinUUID
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class TodoController(userID: UUID) {
    private val user = suspend { User[userID.toJavaUUID()] }
    private val todo: suspend (UUID) -> Todo = { id ->
        user().todos.first {todo ->
            todo.id.value.toKotlinUUID() == id
        }
    }

    suspend fun todos() = newSuspendedTransaction {
        user().todos.map { it.toDTO() }
    }

    suspend fun create(newTodo: com.example.composetodo.dto.Todo) = newSuspendedTransaction {
        val user = user()
        Todo.new(newTodo.id.toJavaUUID()) {
            this.user = user
            title = newTodo.title
            until = newTodo.until.toJavaLocalDateTime()
            finished = newTodo.finished
        }.toDTO()
    }

    suspend fun getTodo(todoID: UUID) = newSuspendedTransaction {
        todo(todoID).toDTO()
    }

    suspend fun delete(todoID: UUID) = newSuspendedTransaction {
        todo(todoID).delete()
    }

    suspend fun update(todoID: UUID, update: com.example.composetodo.dto.Todo) =
        newSuspendedTransaction {
            todo(todoID).apply {
                title = update.title
                until = update.until.toJavaLocalDateTime()
                finished = update.finished
            }.toDTO()
        }
}