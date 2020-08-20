package com.example.composetodo.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import kotlin.random.Random
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

    private suspend fun testing() = newInt()?.toString() ?: "Error"

    private suspend fun newInt(): Int? {
        delay(5.seconds)
        return if (Random.nextBoolean()) {
            42
        } else {
            null
        }
    }
}
