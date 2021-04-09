package app.softwork.composetodo

import kotlinx.html.*
import kotlinx.html.js.*
import org.w3c.dom.*
import react.dom.*

inline fun <T : CommonAttributeGroupFacade> RDOMBuilder<T>.onClick(crossinline block: () -> Unit) {
    attrs { onClickFunction = { _ -> block() } }
}

inline fun RDOMBuilder<INPUT>.onChange(crossinline block: (HTMLInputElement) -> Unit) {
    attrs.onChangeFunction = {
        val target = it.target!! as HTMLInputElement
        block(target)
    }
}

inline fun RDOMBuilder<SELECT>.onChange(crossinline block: (HTMLSelectElement) -> Unit) {
    attrs.onChangeFunction = {
        val target = it.target!! as HTMLSelectElement
        block(target)
    }
}