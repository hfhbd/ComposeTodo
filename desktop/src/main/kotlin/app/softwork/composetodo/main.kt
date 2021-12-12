package app.softwork.composetodo

import androidx.compose.ui.window.*
import kotlinx.coroutines.*

fun main() = application {
    val scope = MainScope()
    val appContainer = DesktopContainer(scope)

    Window(onCloseRequest = {
        scope.cancel()
        exitApplication()
    }) {
        MainView(appContainer)
    }
}
