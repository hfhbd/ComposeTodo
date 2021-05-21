package app.softwork.composetodo

import androidx.compose.web.*
import kotlinx.coroutines.*

val scope = MainScope()

fun main() {
    renderComposable(rootElementId = "root") {
        MainApp()
    }
}
