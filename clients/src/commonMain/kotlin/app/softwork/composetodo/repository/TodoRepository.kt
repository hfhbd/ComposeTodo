package app.softwork.composetodo.repository

import app.softwork.composetodo.*
import com.squareup.sqldelight.db.*
import com.squareup.sqldelight.runtime.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.datetime.*
import kotlinx.uuid.*
import kotlinx.uuid.sqldelight.*

interface TodoRepository {
    companion object {
        operator fun invoke(
            api: API.LoggedIn,
            driver: SqlDriver,
            dao: SchemaQueries = createDatabase(driver).schemaQueries,
            onCreate: (SqlDriver.Schema) -> Unit = { }
        ) = object : TodoRepository {
            init {
                onCreate(ComposeTodoDB.Schema)
            }

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
                dao.upsertTodo(id = UUID(), title = title, until = until, finished = false, recordChangeTag = null)
            }
        }

        fun createDatabase(driver: SqlDriver) =
            ComposeTodoDB(driver, todoAdapter = Todo.Adapter(UUIDStringAdapter, DateTimeAdapter))
    }

    val todos: Flow<List<Todo>>

    suspend fun delete(todo: Todo)

    suspend fun sync()

    suspend fun deleteAll()

    suspend fun create(title: String, until: Instant?)
}
