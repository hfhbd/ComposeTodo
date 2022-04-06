package app.softwork.composetodo

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.*

fun interface Cancelable {
    fun cancel()
}

fun <T> Flow<T>.collectingBlocking(action: (T) -> Unit, onCompletion: (Throwable?) -> Unit): Cancelable =
    collecting(action, onCompletion)

fun <T> Flow<T>.collecting(action: suspend (T) -> Unit, onCompletion: (Throwable?) -> Unit): Cancelable {
    val job = Job()

    CoroutineScope(job).launch {
        try {
            collect {
                action(it)
            }
            onCompletion(null)
        } catch (e: Throwable) {
            onCompletion(e)
            throw e
        }
    }

    return Cancelable {
        job.cancel()
    }
}

fun <T> List<T>.flowFrom() = asFlow()

interface IteratorAsync<out T> : Cancelable {
    /**
     * Returns the next element in the iteration.
     */
    suspend fun next(): T?
}

fun <T> Flow<T>.asAsyncIterable(context: CoroutineContext): IteratorAsync<T> = object : IteratorAsync<T> {
    private var cont: Continuation<Unit>? = null
    private var value: CompletableDeferred<T?> = CompletableDeferred()

    override fun cancel() {
        value.cancel()
        when (val cont = cont) {
            is CancellableContinuation -> {
                if (cont.isActive) {
                    cont.resumeWithException(CancellationException("Canceled upon user request"))
                }
            }
            else -> {
                cont?.resumeWithException(CancellationException("Canceled upon user request"))
            }
        }
    }

    override suspend fun next(): T? {
        val cancelCont = cont
        return if (cancelCont != null && cancelCont is CancellableContinuation) {
            if (cancelCont.isActive) {
                cancelCont.resume(Unit)
                value.await()
            } else null
        } else {
            val collecting = suspend {
                collect { t ->
                    suspendCancellableCoroutine<Unit> {
                        value.complete(t)
                        value = CompletableDeferred()
                        cont = it
                    }
                }
                value.complete(null)
            }
            cont = collecting.createCoroutine(Continuation(context) {
                it.getOrNull()
            })
            cont!!.resume(Unit)
            value.await()
        }
    }
}

fun <T> IteratorAsync<T>.toFlow() = flow {
    while (true) {
        val next = next() ?: break
        emit(next)
    }
}

fun <T> List<T>.async(): IteratorAsync<T> = object : IteratorAsync<T> {
    val iterator = iterator()

    override fun cancel() {}

    override suspend fun next() = if (iterator.hasNext()) iterator.next() else null
}

suspend fun <T> runOnMain(action: suspend () -> T): T = withContext(Dispatchers.Main) {
    action()
}
