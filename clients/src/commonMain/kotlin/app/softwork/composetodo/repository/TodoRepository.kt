package app.softwork.composetodo.repository

import app.softwork.composetodo.*
import com.squareup.sqldelight.db.*
import com.squareup.sqldelight.runtime.coroutines.*
import kotlinx.datetime.*
import kotlinx.uuid.*
import kotlinx.uuid.sqldelight.*

class TodoRepository(
    private val api: API.LoggedIn,
    driver: SqlDriver,
    private val dao: SchemaQueries = createDatabase(driver).schemaQueries
) {
    companion object {
        fun createDatabase(driver: SqlDriver) =
            ComposeTodoDB(driver, todoAdapter = Todo.Adapter(UUIDStringAdapter, DateTimeAdapter))
    }

    val todos = dao.all().asFlow().mapToList()

    suspend fun delete(todo: Todo) {
        dao.delete(id = todo.id)
        api.deleteTodo(todo.id)
    }

    suspend fun sync() {
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

    suspend fun deleteAll() {
        val allIDs = dao.allIDs().executeAsList()
        dao.deleteAll()
        allIDs.forEach { id ->
            api.deleteTodo(id)
        }
    }

    suspend fun create(title: String, until: Instant?) {
        dao.upsertTodo(id = UUID(), title = title, until = until, finished = false, recordChangeTag = null)
    }
}
