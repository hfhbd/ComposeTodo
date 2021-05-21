package app.softwork.composetodo.routing

import androidx.compose.runtime.*
import kotlinx.browser.*

@Composable
fun path(): String {
    var path by remember { mutableStateOf("") }
    SideEffect {
        path = window.location.hash.removePrefix("#")
        println("hash location" + window.location.hash)
        println("history state" + window.history.state)
    }
    return path
}
