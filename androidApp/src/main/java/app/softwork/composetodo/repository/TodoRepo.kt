package app.softwork.composetodo.repository

import app.softwork.composetodo.API
import app.softwork.composetodo.models.Todo
import app.softwork.composetodo.models.TodoEntity

class TodoRepo(
    private val dao: TodoDao,
    private val api: API.LoggedIn
): TodoRepository {
    override suspend fun delete(todo: Todo) {
        dao.delete(todo)
        api.deleteTodo(todo.id)
    }

    override suspend fun get(): List<Todo> = dao.getAll()

    override suspend fun sync(): List<Todo> {
        val remoteRest = api.getTodos()
        val remote = remoteRest.map { TodoEntity(it) }
        val local = dao.getAll()
        val new = local - remote
        dao.insert(remote)
        return dao.getAll()
    }

    override suspend fun deleteAll() {
        dao.getAll().forEach { toDelete ->
            api.delete(toDelete.id)
            dao.delete(toDelete)
        }
    }
}
