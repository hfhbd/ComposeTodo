package app.softwork.composetodo

import kotlinx.html.*
import react.*
import react.dom.*

@HtmlTagMarker
fun RBuilder.navbar() = child(Navbar) { }

private val Navbar = functionalComponent<RProps> {
    nav("navbar navbar-expand-${Breakpoint.Medium} navbar-${Color.Dark} sticky-top bg-${Color.Dark}") {
        Container(Breakpoint.ExtraExtraLarge) {
            Brand {
                +"Softwork.app"
            }
        }
    }
}