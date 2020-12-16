package com.example.composetodo.viewmodels

import com.example.composetodo.TodoRest
import com.example.composetodo.models.Todo
import com.example.composetodo.repository.TodoDao
import com.example.composetodo.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID
import kotlin.test.*
import com.example.composetodo.dto.Todo as RestTodo

internal class TodoViewModelTest {
    private val mainThreadTest = newSingleThreadContext("UI thread")

    @ExperimentalCoroutinesApi
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(mainThreadTest)
    }

    @ExperimentalCoroutinesApi
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadTest.close()
    }
    @Test
    fun loadNew() = runBlocking {
        val repo = TodoRepository(fakeDao, fakeTodoRest)
        val viewModel = TodoViewModel(this, repo)
        val before = viewModel.todos
        assertEquals(before, emptyList())
        repo.sync()
        val after = viewModel.todos
        assertNotEquals(before, after)
        assertTrue(after.size == 1)
        assertEquals(
            after, listOf(
                Todo(
                    id = UUID.generateUUID(),
                    title = "Test",
                    until = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    finished = false
                )
            )
        )
    }

    private val fakeDao = object : TodoDao {
        override suspend fun getAll() = listOf(Todo(
            id = UUID.generateUUID(),
            title = "Test",
            until = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            finished = false
        ))

        override suspend fun getAll(isFinished: Boolean): List<Todo> {
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

        override suspend fun getModified(after: Instant): List<RestTodo>? {
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
