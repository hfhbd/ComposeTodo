package app.softwork.composetodo

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.*

interface IteratorAsync<out T> {
    /**
     * Returns the next element in the iteration.
     */
    suspend fun next(): T?

    fun cancel()
}

fun <T> Flow<T>.asAsyncIterable(context: CoroutineContext): IteratorAsync<T> = object : IteratorAsync<T> {
    private var cont: Continuation<Unit>? = null
    private var value: CompletableDeferred<T?> = CompletableDeferred()

    override fun cancel() {
        value.cancel()
        val started = cont as? CancellableContinuation
        if (started != null && started.isActive) {
            started.resumeWithException(CancellationException("Canceled upon user request"))
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
