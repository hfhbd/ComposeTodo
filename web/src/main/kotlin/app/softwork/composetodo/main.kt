package app.softwork.composetodo

import com.squareup.sqldelight.drivers.sqljs.*
import kotlinx.coroutines.*
import org.jetbrains.compose.web.*

suspend fun main() {
    // https://youtrack.jetbrains.com/issue/KTOR-539
    js(
        """
window.originalFetch = window.fetch;
window.fetch = function (resource, init) {
    return window.originalFetch(resource, Object.assign({ credentials: 'include' }, init || {}));
};
"""
    )
    val driver = initSqlDriver(ComposeTodoDB.Schema).await()
    renderComposable(rootElementId = "root") {
        val appContainer = WebContainer(driver)
        MainApp(appContainer)
    }
}

val scope = MainScope()
