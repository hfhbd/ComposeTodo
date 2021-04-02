package app.softwork.composetodo.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.time.*

class PressMeViewModel(private val scope: CoroutineScope) {

    var text by mutableStateOf("Press Me")

    fun loadNew() {
        scope.launch(Dispatchers.Main) {
            text = testing()
        }
    }

    private suspend fun testing() = newInt()?.toString() ?: "Error"

    @OptIn(ExperimentalTime::class)
    private suspend fun newInt() = withContext(Dispatchers.IO) {
        delay(5.seconds)
        if (Random.nextBoolean()) {
            42
        } else {
            null
        }
    }
}
