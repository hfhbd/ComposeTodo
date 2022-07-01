package app.softwork.composetodo.repository

import app.cash.sqldelight.coroutines.*
import app.cash.sqldelight.db.*
import app.softwork.composetodo.*
import app.softwork.composetodo.dto.*
import kotlinx.coroutines.flow.*
import kotlinx.datetime.*
import kotlinx.uuid.*

interface TodoRepository {
    companion object {
        operator fun invoke(
            api: API.LoggedIn,
            dao: TodoQueries,
        ) = object : TodoRepository {
            override val todos: Flow<List<Todo>> = dao.all().asFlow().mapToList()

            override suspend fun delete(todo: Todo) {
                dao.delete(id = todo.id)
                api.deleteTodo(todo.id)
            }

            override suspend fun sync() {
                api.getTodos().forEach {
                    dao.upsertTodo(
                        id = it.id,
                        title = it.title,
                        until = it.until,
                        finished = it.finished,
                        recordChangeTag = it.recordChangeTag
                    )
                }
            }

            override suspend fun deleteAll() {
                val allIDs = dao.allIDs().executeAsList()
                dao.deleteAll()
                allIDs.forEach { id ->
                    api.deleteTodo(id)
                }
            }

            override suspend fun create(title: String, until: Instant?) {
                val id = TodoDTO.ID(UUID())
                dao.upsertTodo(id = id, title = title, until = until, finished = false, recordChangeTag = null)
            }
        }

        fun createDatabase(driver: SqlDriver) =
            ComposeTodoDB(driver, todoAdapter = Todo.Adapter(IDConverter, DateTimeAdapter))
    }

    val todos: Flow<List<Todo>>

    suspend fun delete(todo: Todo)

    suspend fun sync()

    suspend fun deleteAll()

    suspend fun create(title: String, until: Instant?)
}
