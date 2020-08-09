package com.example.myapplicationresult.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

class PressMeViewModel {

    var text by mutableStateOf("Press Me")

    fun loadNew() {
        GlobalScope.launch(Dispatchers.IO) {
            val newText = testing()
            withContext(Dispatchers.Main) {
                text = newText
            }
        }
    }

    private suspend fun testing() = newInt().fold({
        it.toString()
    }) {
        "Error: ${it.message!!}"
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun newInt(): Result<Int> {
        delay(5.seconds)
        return if (Random.nextBoolean()) {
            Result.success(42)
        } else {
            Result.failure(IllegalStateException("Blubb"))
        }
    }
}
