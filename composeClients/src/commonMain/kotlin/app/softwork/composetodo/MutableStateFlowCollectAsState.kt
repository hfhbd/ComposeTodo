package app.softwork.composetodo

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.*

@Composable
fun <T> MutableStateFlow<T>.collectAsState(
    context: CoroutineContext = EmptyCoroutineContext
): MutableState<T> {
    val state = collectAsState(value, context)
    return object : MutableState<T>, State<T> by state {
        override var value: T
            get() = state.value
            set(value) {
                component2()(value)
            }

        override fun component1(): T = state.value

        override fun component2(): (T) -> Unit = {
            this@collectAsState.value = it
        }
    }
}
