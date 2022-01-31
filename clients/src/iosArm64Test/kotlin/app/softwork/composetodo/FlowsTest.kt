package app.softwork.composetodo

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import kotlin.coroutines.*
import kotlin.test.*

class FlowsTest {

    @Test
    fun flowFrom() = runTest {
        val expected = listOf(1, 2, 3)
        assertEquals(expected, expected.flowFrom().toList())
    }

    @Test
    fun collectTest() = runTest {
        val called = mutableListOf<Int>()
        val expected = listOf(1, 2, 3)
        val results = mutableListOf<Int>()

        suspendCancellableCoroutine<Unit> { cont ->
            val collector = flow {
                repeat(3) {
                    emit(it + 1)
                    called += it + 1
                }
            }.collectingBlocking({
                results.add(it)
            }, {
                assertNull(it)
                cont.resume(Unit)
            })
            cont.invokeOnCancellation {
                collector.cancel()
            }
        }

        assertEquals(expected, actual = results)
        assertEquals(expected, actual = called)
    }

    @Test
    fun collectCancelTest() = runTest {
        val called = mutableListOf<Int>()
        val results = mutableListOf<Int>()

        val job = Job()
        CoroutineScope(job).launch {
            suspendCancellableCoroutine<Unit> { cont ->
                val collector = flow {
                    repeat(3) {
                        emit(it + 1)
                        called += it + 1
                    }
                }.collecting({
                    println("COLLECT $it")
                    results.add(it)
                    if (results.size == 2) {
                        job.cancel()
                    }
                }, {
                    assertNotNull(it)
                })
                cont.invokeOnCancellation {
                    collector.cancel()
                }
            }
        }.join()

        assertEquals(listOf(1, 2), actual = results)
        assertEquals(listOf(1, 2), actual = called)
    }

    @Test
    fun toAsyncTest() = runTest {
        val called = mutableListOf<Int>()
        val expected = flowOf(1, 2, 3).onEach {
            called += it
        }
        val iterator = expected.asAsyncIterable()
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
        val iterator = expected.asAsyncIterable()
        val next = iterator.next()
        assertNotNull(next)
        iterator.cancel()
        assertEquals(1, next)
        assertEquals(
            listOf(1, 2),
            computed,
            "upstream will always emit 1 time because other is a SharedFlow, " +
                "so upstream will be called again before waiting for other"
        )
    }

    @Test
    fun toAsyncCancelNoEmitsTest() = runTest {
        val computed = mutableListOf<Int>()
        val expected = flowOf(1, 2, 3).onEach {
            computed += it
        }
        val iterator = expected.asAsyncIterable()
        iterator.cancel()
        assertEquals(emptyList(), computed)
    }

    @Test
    fun iterableToFlowTest() = runTest {
        val iterable = listOf(1, 2, 3).async()
        assertEquals(listOf(1, 2, 3), iterable.toFlow().toList())
    }
}
