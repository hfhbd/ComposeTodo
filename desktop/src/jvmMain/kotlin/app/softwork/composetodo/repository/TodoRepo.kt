package app.softwork.composetodo.repository

import app.softwork.composetodo.API
import app.softwork.composetodo.models.Todo
import app.softwork.composetodo.models.TodoEntity

class TodoRepo(private val api: API): TodoRepository {
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

    override suspend fun delete(todo: Todo) {
        api.deleteTodo(todo.id)
    }
}
