package app.softwork.composetodo.routing

import androidx.compose.runtime.*
import androidx.compose.web.attributes.*
import androidx.compose.web.elements.*
import kotlinx.browser.*

@Composable
fun NavLink(
    to: String,
    attrs: AttrsBuilder<Tag.A>.() -> Unit,
    content: @Composable () -> Unit
) = A(attrs = {
    attrs()
    onClick {
        window.history.pushState(null, "", "#$to")
    }
}) { content() }
