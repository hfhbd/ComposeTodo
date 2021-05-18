package app.softwork.composetodo.controller

import app.softwork.cloudkitclient.*
import app.softwork.composetodo.*
import app.softwork.composetodo.dao.*
import kotlinx.uuid.*

class TodoController(private val db: Client.Database) {

    suspend fun todos(user: User) = db.query(Todo) {
        Todo.Fields::user eq user
    }.map { it.toDTO() }

    suspend fun create(newTodo: Todo) = db.create(newTodo, Todo)

    suspend fun getTodo(user: User, todoID: UUID) = db.read(todoID.toString(), Todo)?.takeIf {
        it.fields.user.value.recordName == user.recordName
    }

    suspend fun delete(user: User, todoID: UUID) = getTodo(user, todoID)?.let {
        db.delete(it, Todo)
    }

    suspend fun update(user: User, todoID: UUID, update: app.softwork.composetodo.dto.Todo) =
        getTodo(user, todoID)?.toDTO()?.copy(
            title = update.title,
            until = update.until,
            finished = update.finished
        )?.toDAO(user)?.let { db.update(it, Todo) }

    suspend fun deleteAll(user: User) = db.query(Todo) {
        Todo.Fields::user eq user
    }.forEach {
        db.delete(it, Todo)
    }
}
