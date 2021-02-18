package app.softwork.composetodo

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.http.*
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.html.HtmlTagMarker
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.main
import react.dom.nav
import react.dom.render
import react.router.dom.hashRouter
import react.router.dom.route
import react.router.dom.switch

val api = API(HttpClient(Js) {
    install(HttpCookies)
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.todo.softwork.app"
        }
    }
})

val scope = MainScope()

fun main() {
    render(document.getElementById("root")) {
        mainApp()
    }
}

@HtmlTagMarker
fun RBuilder.mainApp() = child(MainApp::class) { }

@HtmlTagMarker
fun RBuilder.navbar() = child(NavBar::class) { }


@HtmlTagMarker
fun RBuilder.users() = child(NavBar::class) { }

@HtmlTagMarker
fun RBuilder.todos() = child(Todos::class) { }

@JsExport
class Todos: RComponent<RProps, RState>() {
    override fun RBuilder.render() {

    }
}

@JsExport
class NavBar : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        nav("navbar navbar-expand-md navbar-dark sticky-top bg-dark") {
            container(Breakpoint.ExtraExtraLarge) {
                brand {
                    +"Softwork.app"
                }
            }
        }
    }
}

@JsExport
class MainApp : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        hashRouter {
            navbar()
            main {
                container {
                    switch {
                        route("/users") {
                            users()
                        }
                        route("/todos") {
                            todos()
                        }
                    }
                }
            }
        }
    }
}