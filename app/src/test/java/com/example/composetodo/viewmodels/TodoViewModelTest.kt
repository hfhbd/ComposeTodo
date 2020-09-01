package com.example.composetodo.viewmodels

import com.example.composetodo.models.Todo
import com.example.composetodo.repository.TodoDao
import com.example.composetodo.repository.TodoRepository
import com.example.composetodo.todo.TodoRest
import kotlinx.coroutines.runBlocking
import java.time.OffsetDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import com.example.composetodo.todo.Todo as RestTodo

internal class TodoViewModelTest {
    @Test
    fun loadNew() = runBlocking {
        val repo = TodoRepository(fakeDao, fakeTodoRest)
        val viewModel = TodoViewModel(this, repo)
        val before = viewModel.todos
        assertEquals(before, emptyList())
        viewModel.loadNew()
        val after = viewModel.todos
        assertNotEquals(before, after)
        assertTrue(after.size == 1)
        assertEquals(after, listOf(Todo(42, "Test", false)))
    }

    private val fakeDao = object : TodoDao {
        override suspend fun getAll(): List<Todo> {
            TODO("Not yet implemented")
        }

        override suspend fun getAll(isCompleted: Boolean): List<Todo> {
            TODO("Not yet implemented")
        }

        override suspend fun update(todo: Todo) {
            TODO("Not yet implemented")
        }

        override suspend fun update(todo: List<Todo>) {
            TODO("Not yet implemented")
        }

        override suspend fun insert(todos: List<Todo>) {
            TODO("Not yet implemented")
        }

        override suspend fun insert(todos: Todo) {
            TODO("Not yet implemented")
        }

        override suspend fun delete(todo: List<Todo>) {
            TODO("Not yet implemented")
        }

        override suspend fun delete(todo: Todo) {
            TODO("Not yet implemented")
        }

        override suspend fun deleteAll() {
            TODO("Not yet implemented")
        }

    }

    private val fakeTodoRest = object : TodoRest {
        override suspend fun getAll(): List<RestTodo>? {
            TODO("Not yet implemented")
        }

        override suspend fun getModified(after: OffsetDateTime): List<RestTodo>? {
            TODO("Not yet implemented")
        }

        override suspend fun update(todo: RestTodo): RestTodo? {
            TODO("Not yet implemented")
        }

        override suspend fun delete(todo: RestTodo): Unit? {
            TODO("Not yet implemented")
        }
    }
}
