package app.softwork.composetodo

import app.softwork.composetodo.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.datetime.*
import kotlinx.uuid.*

class OnlineRepository(val api: API.LoggedIn) : TodoRepository {
    override val todos: MutableStateFlow<List<Todo>> = MutableStateFlow(emptyList())

    override suspend fun delete(todo: Todo) {
        todos.value -= todo
        api.deleteTodo(todo.id)
    }

    override suspend fun sync() {
        todos.value = api.getTodos().map { it.toDomain() }
    }

    override suspend fun deleteAll() {
        val old = todos.value
        todos.value = emptyList()
        old.forEach {
            api.deleteTodo(it.id)
        }
    }

    private fun app.softwork.composetodo.dto.Todo.toDomain(): Todo = Todo(
        id = id,
        title = title,
        finished = finished,
        recordChangeTag = recordChangeTag,
        until = until
    )

    override suspend fun create(title: String, until: Instant?) {
        val newTodo = app.softwork.composetodo.dto.Todo(
            id = UUID(),
            title = title,
            finished = false,
            until = until,
            recordChangeTag = null
        )
        todos.value += newTodo.toDomain()
        api.createTodo(newTodo)
    }
}
