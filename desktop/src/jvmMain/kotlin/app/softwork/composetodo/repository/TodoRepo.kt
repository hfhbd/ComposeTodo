package app.softwork.composetodo.repository

import app.softwork.composetodo.*
import app.softwork.composetodo.models.*
import kotlinx.datetime.*
import kotlinx.uuid.*

class TodoRepo(private val api: API.LoggedIn) : TodoRepository<TodoEntity> {
    override suspend fun getRemote() = api.getTodos().map { TodoEntity(it) }

    /**
     * Always online, no local DB yet
     */
    override suspend fun sync() = getRemote()

    override suspend fun deleteAll() {
        val all = getRemote()
        all.forEach { toDelete ->
            api.deleteTodo(toDelete.id)
        }
    }

    override suspend fun delete(todo: TodoEntity) {
        api.deleteTodo(todo.id)
    }

    override suspend fun create(title: String, until: Instant?) {
        api.createTodo(
            TodoEntity(
                title = title,
                until = until,
                finished = false,
                id = UUID(),
                recordChangeTag = null
            ).toDTO()
        )
    }
}
