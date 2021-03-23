package app.softwork.composetodo.repository

import app.softwork.composetodo.*
import app.softwork.composetodo.models.*

class TodoRepo(private val api: API): TodoRepository<TodoEntity> {
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
}
