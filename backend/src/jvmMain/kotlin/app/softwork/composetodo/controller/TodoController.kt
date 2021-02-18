package app.softwork.composetodo.controller

import app.softwork.composetodo.toDTO
import app.softwork.composetodo.dao.Todo
import app.softwork.composetodo.dao.User
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.uuid.UUID
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class TodoController(userID: UUID) {
    private val user = suspend { User[userID] }
    private val todo: suspend (UUID) -> Todo = { id ->
        user().todos.first { todo ->
            todo.id.value == id
        }
    }

    suspend fun todos() = newSuspendedTransaction {
        user().todos.map { it.toDTO() }
    }

    suspend fun create(newTodo: app.softwork.composetodo.dto.Todo) = newSuspendedTransaction {
        val user = user()
        Todo.new(newTodo.id) {
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

    suspend fun update(todoID: UUID, update: app.softwork.composetodo.dto.Todo) =
        newSuspendedTransaction {
            todo(todoID).apply {
                title = update.title
                until = update.until.toJavaLocalDateTime()
                finished = update.finished
            }.toDTO()
        }
}