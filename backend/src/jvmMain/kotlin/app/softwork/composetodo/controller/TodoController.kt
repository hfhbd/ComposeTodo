package app.softwork.composetodo.controller

import app.softwork.composetodo.*
import app.softwork.composetodo.dao.*
import app.softwork.composetodo.definitions.*
import kotlinx.datetime.*
import kotlinx.uuid.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.*

class TodoController(val user: User) {
    suspend fun todos() = newSuspendedTransaction {
        user.todos.map { it.toDTO() }
    }

    suspend fun create(newTodo: app.softwork.composetodo.dto.Todo) = newSuspendedTransaction {
        Todo.new(newTodo.id) {
            this.user = user
            title = newTodo.title
            until = newTodo.until?.toJavaLocalDateTime()
            finished = newTodo.finished
        }.toDTO()
    }

    suspend fun getTodo(todoID: UUID) = newSuspendedTransaction {
        Todo.find {
            Todos.id eq todoID and (Todos.user eq user.id)
        }.first().toDTO()
    }

    suspend fun delete(todoID: UUID) = newSuspendedTransaction {
        Todo.find {
            Todos.id eq todoID and (Todos.user eq user.id)
        }.first().delete()
    }

    suspend fun update(todoID: UUID, update: app.softwork.composetodo.dto.Todo) =
        newSuspendedTransaction {
            Todo.find {
                Todos.id eq todoID and (Todos.user eq user.id)
            }.first().apply {
                title = update.title
                until = update.until?.toJavaLocalDateTime()
                finished = update.finished
            }.toDTO()
        }

    suspend fun deleteAll() = newSuspendedTransaction {
        user.todos.forEach {
            it.delete()
        }
    }
}
