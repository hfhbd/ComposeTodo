package app.softwork.composetodo

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import app.softwork.routingcompose.*
import org.jetbrains.compose.web.dom.*

@Composable
fun Navbar(links: List<Pair<String, String>>, onLogout: (() -> Unit)?) {
    Nav(attrs = {
        classes(
            "navbar",
            "navbar-expand-${Breakpoint.Medium}",
            "navbar-${Color.Dark}",
            "sticky-top",
            "bg-${Color.Dark}"
        )
    }) {
        Container(fluid = true, Breakpoint.ExtraExtraLarge) {
            Brand {
                Text("Softwork.app")
            }

            if (onLogout != null) {
                Toggler("navbarNav", "navbarNav")
                Div(attrs = {
                    classes("collapse", "navbar-collapse")
                    id("navbarNav")
                }) {
                    Ul(attrs = { classes("navbar-nav", "me-auto") }) {
                        links.forEach { (name, link) ->
                            Li(attrs = { classes("nav-item") }) {
                                NavLink(attrs = { classes("nav-link") }, to = link) { Text(name) }
                            }
                        }
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
    }
}
