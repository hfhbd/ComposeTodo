package app.softwork.composetodo.users

import kotlinx.html.*
import react.*

@HtmlTagMarker
fun RBuilder.users() = child(Users) { }

private val Users = functionalComponent<RProps> {

}