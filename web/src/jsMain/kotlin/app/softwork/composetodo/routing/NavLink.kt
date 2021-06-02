package app.softwork.composetodo.routing

import androidx.compose.runtime.*
import kotlinx.browser.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*

@Composable
fun NavLink(
    to: String,
    attrs: AttrsBuilder<Tag.A>.() -> Unit,
    content: @Composable () -> Unit
) = A(attrs = {
    attrs()
    onClick {
        HashRouter.push(to)
    }
}) { content() }

object HashRouter {
    private var subCounter = 0
    private val subscriber: MutableMap<Int, (String) -> Unit> = mutableMapOf()

    fun subscribe(block: (String) -> Unit): Int {
        subscriber[subCounter] = block
        return subCounter.also {
            subCounter += 1
        }
    }

    fun removeSubscription(id: Int) {
        subscriber.remove(id)
    }

    fun push(value: String) {
        subscriber.entries.forEach { (_, fn) ->
            fn(value)
        }
        window.history.pushState(null, title = "", "#$value")
    }
}

@Composable
fun path(key: Any?, defaultPath: String = window.location.hash.removePrefix("#")): State<String> {
    val path = remember { mutableStateOf(defaultPath) }
    DisposableEffect(key) {
        val id = HashRouter.subscribe {
            println("new state $it")
            path.value = it
        }
        onDispose {
            HashRouter.removeSubscription(id)
        }
    }
    println("new state3 ${path.value}")
    return path
}