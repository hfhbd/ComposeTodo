package app.softwork.composetodo

import kotlinx.html.*
import org.w3c.dom.*
import react.*
import react.dom.*
import kotlin.contracts.*

@DslMarker
annotation class BootstrapDSL

@BootstrapDSL
fun RBuilder.Toggler(target: String, controls: String) {
    button(classes = "navbar-toggler", type = ButtonType.button) {
        attrs["data-toggle"] = "collapse"
        attrs["data-target"] = "#$target"
        attrs["aria-controls"] = controls
        attrs["aria-expanded"] = "false"
        attrs["aria-label"] = "Toggle navigation"

        span(classes = "navbar-toggler-icon") { }
    }
}

enum class Breakpoint(private val classInfix: String) {
    Small("sm"),
    Medium("md"),
    Large("lg"),
    ExtraLarge("xl"),
    ExtraExtraLarge("xxl");

    override fun toString() = classInfix
}

enum class Color(private val value: String) {
    Primary("primary"),
    Secondary("secondary"),
    Success("success"),
    Info("info"),
    Warning("warning"),
    Danger("danger"),
    Light("light"),
    Dark("dark"),

    Blue("blue"),
    Indigo("indigo"),
    Purple("purple"),
    Pink("pink"),
    Red("red"),
    Orange("orange"),
    Yellow("yellow"),
    Green("green"),
    Teal("teal"),
    Cyan("cyan"),
    White("white"),
    Gray("gray-600"),
    GrayDark("gray-800");

    override fun toString() = value
}

@BootstrapDSL
fun RBuilder.Brand(block: RDOMBuilder<DIV>.() -> Unit) = div("navbar-brand", block)

@BootstrapDSL
fun RBuilder.Row(block: RDOMBuilder<DIV>.() -> Unit) = div("row", block)

@BootstrapDSL
fun RBuilder.Column(
    breakpoint: Breakpoint? = null,
    size: Int? = null,
    block: RDOMBuilder<DIV>.() -> Unit
) =
    div("col${breakpoint?.let { "-$it" } ?: ""}${size?.let { "-$it" } ?: ""}", block)

@BootstrapDSL
fun RBuilder.Container(
    type: Breakpoint? = null,
    block: RDOMBuilder<DIV>.() -> Unit
) =
    div("container${type?.let { "-$it" } ?: ""}", block)

@BootstrapDSL
inline fun RBuilder.Input(
    type: InputType? = null,
    label: String,
    labelClasses: String = "form-label",
    inputClasses: String = "form-control",
    placeholder: String,
    value: String,
    crossinline block: RDOMBuilder<INPUT>.() -> Unit = { },
    crossinline attrs: INPUT.() -> Unit = { },
    crossinline onChange: (HTMLInputElement) -> Unit
) = label(classes = labelClasses) {
    +label
    input(type = type, classes = inputClasses) {
        attrs {
            this.value = value
            this.placeholder = placeholder
            block()
            attrs()
        }
        onChange(onChange)
    }
}


@BootstrapDSL
inline fun RBuilder.Button(
    title: String,
    color: Color = Color.Primary,
    customClasses: String = "",
    type: ButtonType = ButtonType.submit,
    crossinline block: RDOMBuilder<BUTTON>.() -> Unit = { },
    crossinline attrs: BUTTON.() -> Unit = { },
    crossinline onClick: () -> Unit
) = button(classes = "btn btn-$color $customClasses", type = type) {
    +title
    block()
    attrs {
        attrs()
    }
    onClick(onClick)
}

data class Row(val id: String, val color: Color?, val cells: List<Cell>) {
    data class Cell(val color: Color?, val block: RDOMBuilder<TD>.() -> Unit)

    class Builder(val id: String) {
        private val values = mutableListOf<Pair<String, Cell>>()

        var rowColor: Color? = null

        fun build() = values to rowColor

        fun cell(title: String, color: Color? = null, block: RDOMBuilder<TD>.() -> Unit) {
            values.add(title to Cell(color, block))
        }
    }
}

@BootstrapDSL
fun <T> RBuilder.Table(
    data: List<T>, id: (T) -> String,
    color: Color? = null,
    striped: Boolean = false,
    hover: Boolean = false,
    map: Row.Builder.(T) -> Unit
): ReactElement {
    val headers = mutableListOf<String>()
    val rows = mutableListOf<Row>()

    fun add(rowBuilder: Row.Builder) {
        val (rowValues, rowColor) = rowBuilder.build()
        val cells = rowValues.map { (header, cellValue) ->
            if (header !in headers) {
                headers.add(header)
            }
            cellValue
        }
        val row = Row(rowBuilder.id, rowColor, cells)
        rows.add(row)
    }

    data.forEach {
        val row = Row.Builder(id(it)).apply { map(it) }
        add(row)
    }
    return Table(
        color = color,
        striped = striped,
        hover = hover,
        headers = headers,
        rows = rows
    )
}

@BootstrapDSL
fun RBuilder.Table(
    color: Color? = null,
    striped: Boolean = false,
    hover: Boolean = false,
    headers: List<String>,
    rows: List<Row>
) =
    table(classes = "table ${color?.let { "table-$it" }} ${"table-hover".takeIf { hover }} ${"table-striped".takeIf { striped }}") {
        thead {
            tr {
                for (header in headers) {
                    th {
                        attrs["scope"] = "col"
                        +header
                    }
                }
            }
        }
        tbody {
            rows.renderEach { row ->
                tr(classes = "table${row.color?.let { "-$it" }}") {
                    attrs.key = row.id
                    for (cell in row.cells) {
                        td(classes = "table${cell.color?.let { "-$it" }}") {
                            cell.block(this)
                        }
                    }
                }
            }
        }
    }
