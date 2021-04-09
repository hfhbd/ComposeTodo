package app.softwork.composetodo

import app.softwork.composetodo.todos.*
import app.softwork.composetodo.users.*
import kotlinx.html.*
import react.*
import react.dom.*
import react.router.dom.*

@HtmlTagMarker
fun RBuilder.mainApp() = child(MainApp)

private val MainApp = functionalComponent<RProps> {
    hashRouter {
        navbar()
        main {
            Container {
                switch {
                    route("/users") {
                        users()
                    }
                    route("/") {
                        todos()
                    }
                }
            }
        }
    }
}