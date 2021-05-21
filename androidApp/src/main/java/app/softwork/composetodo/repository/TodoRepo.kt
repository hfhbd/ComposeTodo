package app.softwork.composetodo.repository

import app.softwork.composetodo.*
import app.softwork.composetodo.models.*
import kotlinx.datetime.*
import kotlinx.uuid.*

class TodoRepo(
    private val dao: TodoDao,
    private val api: API.LoggedIn
) : TodoRepository<TodoEntity> {
    override suspend fun delete(todo: TodoEntity) {
        dao.delete(todo)
        api.deleteTodo(todo.id)
    }

    override suspend fun sync(): List<TodoEntity> {
        val remoteRest = api.getTodos()
        val remote = remoteRest.map { TodoEntity(it) }
        val local = dao.getAll()
        val new = local - remote
        dao.insert(remote)
        return dao.getAll()
    }

    override suspend fun deleteAll() {
        dao.getAll().forEach { toDelete ->
            api.deleteTodo(toDelete.id)
            dao.delete(toDelete)
        }
    }

    override suspend fun create(title: String, until: Instant?) {
        val newTodo = TodoEntity(
            id = UUID(),
            title = title,
            until = until,
            finished = false,
            recordChangeTag = null
        )
        dao.insert(todo = newTodo)
    }
}
