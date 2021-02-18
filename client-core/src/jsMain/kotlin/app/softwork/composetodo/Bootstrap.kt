package app.softwork.composetodo

import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.MAIN
import kotlinx.html.NAV
import react.dom.RDOMBuilder
import react.dom.button
import react.dom.div
import react.dom.span

@DslMarker
annotation class BootstrapDSL

@BootstrapDSL
fun RDOMBuilder<DIV>.toggler() {
    button(classes = "navbar-toggler", type = ButtonType.button) {
        attrs["data-toggle"] = "collapse"
        attrs["data-target"] = "#navbarNav"
        attrs["aria-controls"] = "navbarNav"
        attrs["aria-expanded"] = "false"
        attrs["aria-label"] = "Toggle navigation"

        span(classes = "navbar-toggler-icon") { }
    }
}

enum class Breakpoint(val classInfix: String) {
    Small("sm"),
    Medium("md"),
    Large("lg"),
    ExtraLarge("xl"),
    ExtraExtraLarge("xxl")
}

@BootstrapDSL
fun RDOMBuilder<DIV>.brand(block: RDOMBuilder<DIV>.() -> Unit) = div("navbar-brand", block)

@BootstrapDSL
fun RDOMBuilder<DIV>.row(block: RDOMBuilder<DIV>.() -> Unit) = div("row", block)

@BootstrapDSL
fun RDOMBuilder<DIV>.col(breakpoint: Breakpoint? = null, size: Int? = null, block: RDOMBuilder<DIV>.() -> Unit) =
    div("col${breakpoint?.let { "-${it.classInfix}" }?: ""}${size?.let { "-$it" }?: ""}", block)

@BootstrapDSL
fun RDOMBuilder<MAIN>.container(block: RDOMBuilder<DIV>.() -> Unit) = div("container", block)

@BootstrapDSL
fun RDOMBuilder<NAV>.container(type: Breakpoint? = null, block: RDOMBuilder<DIV>.() -> Unit) =
    div("container${type?.let { "-${it.classInfix}" } ?: ""}", block)
