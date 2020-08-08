package com.example.myapplicationresult

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Border
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.graphics.Shape
import androidx.ui.layout.InnerPadding
import androidx.ui.layout.Row
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.contentColorFor
import androidx.ui.unit.Dp
import androidx.ui.unit.Duration
import androidx.ui.unit.dp
import androidx.ui.unit.inMilliseconds
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var text by state { "Press Me" }
            Row {
                Button("Hello World") {
                    testing()
                }
                Button({
                    val newText = testing()
                    withContext(Dispatchers.Main) {
                        text = newText
                    }
                }, dispatcher = Dispatchers.IO) {
                    Text(text)
                }
            }
        }
    }

    fun io(action: suspend () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) { action() }
    }

    suspend fun testing(): String = newInt() fold {
        onFailure {
            "Error: ${it.message!!}"
        }
        onSuccess {
            it.toString()
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


infix fun <T, R> Result<T>.fold(folding: Folding<T, R>.() -> Unit): R {
    val f = Folding<T, R>().apply { folding() }
    return fold(f.success, f.failure)
}

operator fun <T, R> Result<T>.invoke(folding: Folding<T, R>.() -> Unit) = fold(folding)

class Folding<T, R> {
    lateinit var success: (T) -> R
    lateinit var failure: (Throwable) -> R

    fun onSuccess(block: (T) -> R) {
        success = block
    }

    fun onFailure(block: (Throwable) -> R) {
        failure = block
    }
}

@Composable
fun Button(initial: String, dispatcher: CoroutineDispatcher = Dispatchers.IO, onClick: suspend () -> String) {
    var content by state { initial }
    Button(onClick = {
        GlobalScope.launch(dispatcher) {
            val newValue = onClick()
            withContext(Dispatchers.Main) {
                content = newValue
            }
        }
    }) {
        Text(content)
    }
}

@Composable
fun Button(
    suspendOnClick: suspend () -> Unit,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    elevation: Dp = 2.dp,
    disabledElevation: Dp = 0.dp,
    shape: Shape = MaterialTheme.shapes.small,
    border: Border? = null,
    backgroundColor: Color = MaterialTheme.colors.primary,
    disabledBackgroundColor: Color = Button.defaultDisabledBackgroundColor,
    contentColor: Color = contentColorFor(backgroundColor),
    disabledContentColor: Color = Button.defaultDisabledContentColor,
    padding: InnerPadding = Button.DefaultInnerPadding,
    text: @Composable () -> Unit
) = Button(
    { GlobalScope.launch(dispatcher) { suspendOnClick() } },
    modifier,
    enabled,
    elevation,
    disabledElevation,
    shape,
    border,
    backgroundColor,
    disabledBackgroundColor,
    contentColor,
    disabledContentColor,
    padding,
    text
)
