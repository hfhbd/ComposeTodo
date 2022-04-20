package app.softwork.composetodo

import androidx.compose.runtime.*
import androidx.compose.ui.window.*

fun main() = singleWindowApplication {
    val appContainer = remember { DesktopContainer() }

    MainView(appContainer)
}
