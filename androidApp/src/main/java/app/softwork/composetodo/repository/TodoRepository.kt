package app.softwork.composetodo.repository

import app.softwork.composetodo.API
import app.softwork.composetodo.models.Todo

class TodoRepository(
    private val dao: TodoDao,
    private val api: API.LoggedIn
) {
    suspend fun delete(todo: Todo) {
        dao.delete(todo)
        api.deleteTodo(todo.id)
    }

    suspend fun get(): List<Todo> = dao.getAll()

    suspend fun sync(): List<Todo> {
        val remoteRest = api.getTodos()
        val remote = remoteRest.map { Todo(it) }
        val local = dao.getAll()
        val new = local - remote
        dao.insert(remote)
        return dao.getAll()
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}
