package com.example.myapplicationresult

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.material.Button
import androidx.ui.unit.Duration
import androidx.ui.unit.inMilliseconds
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Button("Hello World") {
                testing()
            }
            Button({
                io {
                    println(testing())
                }
            }) {
                Text("Press me")
            }
        }
    }

    fun io(action: suspend () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) { action() }
    }

    fun <T> MutableState<T>.IO(action: suspend () -> T) {
        GlobalScope.launch(Dispatchers.IO) {
            val newValue = action()
            withContext(Dispatchers.Main) {
                value = newValue
            }
        }
    }

    suspend fun testing(): String {
        val s = newInt()
        return s.fold({ it.toString() }) {
            it.message!!
        }
    }

    suspend fun newInt(): Result<Int> {
        delay(Duration(seconds = 5).inMilliseconds())
        return if (Boolean.random()) {
            Result.success(42)
        } else {
            Result.failure(IllegalStateException("Blubb"))
        }
    }

    fun Boolean.Companion.random(): Boolean {
        return (0..1).random().toBoolean()
    }

    fun Int.toBoolean() = when (this) {
        1 -> true
        else -> false
    }
}

@Composable
fun Button(initial: String, dispatcher: CoroutineDispatcher = Dispatchers.IO, onClick: suspend () -> String) {
    val content = state { initial }
    Button(onClick = {
        GlobalScope.launch(dispatcher) {
            val newValue = onClick()
            withContext(Dispatchers.Main) {
                content.value = newValue
            }
        }
    }) {
        Text(content.value)
    }
}
