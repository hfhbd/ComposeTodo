package app.softwork.composetodo

import app.cash.sqldelight.driver.worker.*
import kotlinx.coroutines.*
import org.jetbrains.compose.web.*
import org.w3c.dom.*

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
    val driver = WebWorkerDriver(
        Worker(js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)"""))
    )
    ComposeTodoDB.Schema.migrate(driver, 0, 1).await()
    renderComposable(rootElementId = "root") {
        val appContainer = WebContainer(driver)
        MainApp(appContainer)
    }
}

val scope = MainScope()
