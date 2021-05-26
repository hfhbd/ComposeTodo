package app.softwork.composetodo

import androidx.compose.web.*
import kotlinx.coroutines.*

val scope = MainScope()

fun main() {
    // https://youtrack.jetbrains.com/issue/KTOR-539
    js(
        """
window.originalFetch = window.fetch;
window.fetch = function (resource, init) {
    return window.originalFetch(resource, Object.assign({ credentials: 'include' }, init || {}));
};
"""
    )
    renderComposable(rootElementId = "root") {
        MainApp()
    }
}
