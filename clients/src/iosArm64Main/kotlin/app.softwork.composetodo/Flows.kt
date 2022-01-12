package app.softwork.composetodo

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*

fun interface Cancelable {
    fun cancel()
}

fun <T> Flow<T>.collectingBlocking(action: (T) -> Unit, onCompletion: (Throwable?) -> Unit): Cancelable =
    collecting(action, onCompletion)

fun <T> Flow<T>.collecting(action: suspend (T) -> Unit, onCompletion: (Throwable?) -> Unit): Cancelable {
    val job = Job()

    onEach {
        action(it)
    }.onCompletion {
        onCompletion(it)
    }.launchIn(CoroutineScope(job))

    return Cancelable {
        job.cancel()
    }
}

fun <T> List<T>.flowFrom() = asFlow()

public interface IteratorAsync<out T> : Cancelable {
    /**
     * Returns the next element in the iteration.
     */
    public suspend fun next(): T?
}

fun <T> Flow<T>.asAsyncIterable(): IteratorAsync<T> = object : IteratorAsync<T> {
    private val job = Job()
    private val requester = MutableSharedFlow<suspend (T) -> Unit>()

    init {
        onCompletion {
            job.cancel()
        }.zip(requester) { t, requester ->
            requester(t)
        }.launchIn(CoroutineScope(job))
    }

    override fun cancel() {
        job.cancel()
    }

    override suspend fun next(): T? = if (job.isCompleted) {
        null
    } else {
        callbackFlow {
            requester.emit {
                send(it)
            }
            job.invokeOnCompletion {
                trySend(null)
            }
            awaitClose()
        }.first()
    }
}
