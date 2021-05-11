package app.softwork.composetodo.controller

import app.softwork.composetodo.*
import app.softwork.composetodo.dao.*
import app.softwork.composetodo.definitions.*
import kotlinx.datetime.*
import kotlinx.uuid.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.*

class TodoController(private val db: Database) {
    suspend fun todos(user: User) = newSuspendedTransaction(db = db) {
        user.todos.map { it.toDTO() }
    }

    suspend fun create(user: User, newTodo: app.softwork.composetodo.dto.Todo) = newSuspendedTransaction(db = db) {
        Todo.new(newTodo.id) {
            this.user = user
            title = newTodo.title
            until = newTodo.until?.toJavaLocalDateTime()
            finished = newTodo.finished
        }.toDTO()
    }

    suspend fun getTodo(user: User, todoID: UUID) = newSuspendedTransaction(db = db) {
        Todo.find {
            Todos.id eq todoID and (Todos.user eq user.id)
        }.first().toDTO()
    }

    suspend fun delete(user: User, todoID: UUID) = newSuspendedTransaction(db = db) {
        Todo.find {
            Todos.id eq todoID and (Todos.user eq user.id)
        }.first().delete()
    }

    suspend fun update(user: User, todoID: UUID, update: app.softwork.composetodo.dto.Todo) =
        newSuspendedTransaction(db = db) {
            Todo.find {
                Todos.id eq todoID and (Todos.user eq user.id)
            }.first().apply {
                title = update.title
                until = update.until?.toJavaLocalDateTime()
                finished = update.finished
            }.toDTO()
        }

    suspend fun deleteAll(user: User) = newSuspendedTransaction(db = db) {
        user.todos.forEach {
            it.delete()
        }
    }
}
