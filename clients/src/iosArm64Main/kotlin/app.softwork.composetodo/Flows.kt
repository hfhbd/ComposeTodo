package app.softwork.composetodo

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import platform.Foundation.*

fun <T> toFlow(caller: String, collector: ((NSError?) -> Unit, suspend (T) -> Unit) -> Unit): Flow<T> =
    flow {
        withContext(Dispatchers.Main) {
            collector({
                if (it != null) {
                    throw Error(it.localizedDescription)
                }
            }) {
                withContext(Dispatchers.Main) {
                    emit(it)
                }
            }
        }
    }.flowOn(Dispatchers.Main)

suspend fun <T> Flow<T>.collectOnMain(collector: (T) -> Unit) = withContext(Dispatchers.Main) {
    flowOn(Dispatchers.Main)
        .collect {
            collector(it)
        }
}
