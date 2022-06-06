package app.softwork.composetodo

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import app.softwork.bootstrapcompose.NavbarCollapseBehavior.*
import app.softwork.routingcompose.*
import org.jetbrains.compose.web.dom.*

@Composable
fun Navbar(links: List<Pair<String, String>>, onLogout: (() -> Unit)?) {
    Navbar(
        fluid = true,
        colorScheme = Color.Dark,
        placement = NavbarPlacement.StickyTop,
        backgroundColor = Color.Dark,
        collapseBehavior = Never,
        toggler = false,
        brand = {
            Brand {
                Text("Softwork.app")
            }
        },
        navAttrs = {
            classes("me-auto")
        }
    ) {
        if (onLogout != null) {
            for ((name, link) in links) {
                NavLink(attrs = {
                    classes("nav-link")
                }, to = link) { Text(name) }
            }
            A(attrs = {
                classes("btn", "btn-outline-${Color.Light}")
                onClick {
                    onLogout()
                }
            }, href = "/#/") { Text("Logout") }
        }
    }
}
