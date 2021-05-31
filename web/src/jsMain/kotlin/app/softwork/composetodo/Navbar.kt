package app.softwork.composetodo

import androidx.compose.runtime.*
import androidx.compose.web.elements.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.routing.*
import kotlinx.coroutines.*

@Composable
fun Navbar(api: API, onLogout: () -> Unit) {
    Nav(attrs = {
        classes("navbar", "navbar-expand-${Breakpoint.Medium}", "navbar-${Color.Dark}", "sticky-top", "bg-${Color.Dark}")
    }) {
        Container(Breakpoint.ExtraExtraLarge) {
            Brand {
                Text("Softwork.app")
            }
        }
        if (api is API.LoggedIn) {

            Toggler("navbarNav", "navbarNav")
            Div(attrs = {
                classes("collapse", "navbar-collapse")
                id("navbarNav")
            }) {
                Ul(attrs = { classes("navbar-nav", "mr-auto") }) {
                    Li(attrs = { classes("nav-item") }) {
                        NavLink(attrs = { classes("nav-link") }, to = "/") {
                            Text("Tours")
                        }
                    }
                    Li(attrs = { classes("nav-item") }) {
                        NavLink(attrs = { classes("nav-link") }, to = "/users") {
                            Text("Users")
                        }
                    }
                }
                A(attrs = {
                    classes("btn", "btn-outline-${Color.Light}")
                    onClick {
                        scope.launch {
                            api.logout()
                            onLogout()
                        }
                    }
                }, href = "/#") {
                    Text("Logout")
                }
            }
        }
    }
}
