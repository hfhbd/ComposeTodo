package app.softwork.composetodo

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@ExperimentalCoroutinesApi
class FlowsTest {
    @Test
    fun toAsyncTest() = runTest {
        val called = mutableListOf<Int>()
        val expected = flowOf(1, 2, 3).onEach {
            called += it
        }
        val iterator = expected.asAsyncIterable(coroutineContext)
        val values = buildList {
            while (true) {
                val next = iterator.next() ?: break
                add(next)
            }
        }
        assertEquals(listOf(1, 2, 3), values)
        assertEquals(listOf(1, 2, 3), called)
    }

    @Test
    fun toAsyncCancelTest() = runTest {
        val computed = mutableListOf<Int>()
        val expected = flowOf(1, 2, 3).onEach {
            computed += it
        }
        val iterator = expected.asAsyncIterable(coroutineContext)
        val next = iterator.next()
        assertNotNull(next)
        iterator.cancel()
        assertEquals(1, next)
        assertEquals(listOf(1), computed)
    }

    @Test
    fun toAsyncCancelNoEmitsTest() = runTest {
        val computed = mutableListOf<Int>()
        val expected = flowOf(1, 2, 3).onEach {
            computed += it
        }
        val iterator = expected.asAsyncIterable(coroutineContext)
        iterator.cancel()
        assertEquals(emptyList(), computed)
    }
}
