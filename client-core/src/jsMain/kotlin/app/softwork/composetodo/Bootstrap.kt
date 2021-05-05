package app.softwork.composetodo

import androidx.compose.runtime.*
import androidx.compose.web.attributes.*
import androidx.compose.web.css.*
import androidx.compose.web.elements.*
import androidx.compose.web.elements.Text
import org.w3c.dom.*

@Composable
fun Toggler(target: String, controls: String) {
    Button(attrs = {
        classes("navbar-toggler")
        attr("data-toggle", "collapse")
        attr("data-target", "#$target")
        attr("aria-controls", controls)
        attr("aria-expanded", "false")
        attr("aria-label", "Toggle navigation")
    }) {
        Span(attrs = { classes("navbar-toggler-icon") }) { }
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

@Composable
fun Brand(content: @Composable () -> Unit) = Div(attrs = {
    classes("navbar-brand")
}) {
    content()
}

@Composable
fun Row(content: @Composable () -> Unit) = Div(attrs = { classes("row") }) { content() }

@Composable
fun Column(
    breakpoint: Breakpoint? = null,
    size: Int? = null,
    content: @Composable () -> Unit
) =
    Div(attrs = { classes("col${breakpoint?.let { "-$it" } ?: ""}${size?.let { "-$it" } ?: ""}") }) {
        content()
    }

@Composable
fun Container(
    type: Breakpoint? = null,
    content: @Composable () -> Unit
) = Div(attrs = {
    classes("container${type?.let { "-$it" } ?: ""}")
}) {
    content()
}

@Composable
inline fun DateTimeInput(
    label: String,
    labelClasses: String = "form-label",
    inputClasses: String = "form-control",
    placeholder: String,
    value: String,
    crossinline attrs: AttrsBuilder<Tag.Input>.() -> Unit = { },
    crossinline onChange: (HTMLInputElement) -> Unit
) = Label(forId = "", attrs = {
    classes(labelClasses)
    attr("for", null)
}) {
    Text(label)
    Input(type = InputType.DateTimeLocal, attrs = {
        attrs()
        classes(inputClasses)
        value(value)
        placeholder(placeholder)
        addEventListener("input") {
            onChange(it.nativeEvent.target as HTMLInputElement)
        }
    })
}


@Composable
inline fun input(
    type: InputType = InputType.Text,
    label: String,
    labelClasses: String = "form-label",
    inputClasses: String = "form-control",
    placeholder: String,
    value: String,
    crossinline attrs: AttrsBuilder<Tag.Input>.() -> Unit = { },
    crossinline onChange: (HTMLInputElement) -> Unit
) = Label(forId = "", attrs = {
    classes(labelClasses)
    attr("for", null)
}) {
    require(type != InputType.DateTimeLocal)
    Text(label)
    Input(type = type, attrs = {
        attrs()
        classes(inputClasses)
        value(value)
        placeholder(placeholder)
        this.onInput {
            val target = it.nativeEvent.target as HTMLInputElement
            onChange(target)
        }
    })
}


@Composable
inline fun button(
    title: String,
    color: Color = Color.Primary,
    vararg customClasses: String = emptyArray(),
    type: ButtonType = ButtonType.Submit,
    crossinline attrs: AttrsBuilder<Tag.Button>.() -> Unit = { },
    crossinline onClick: () -> Unit
) = Button(attrs = {
    classes("btn", "btn-$color", *customClasses)
    attrs()
    type(type)
    onClick {
        onClick()
    }
}) {
    Text(title)
}

data class Row(val color: Color?, val cells: List<Cell>) {
    data class Cell(val color: Color?, val content: @Composable () -> Unit)

    class Builder {
        private val values = mutableListOf<Pair<String, Cell>>()

        var rowColor: Color? = null

        fun build() = values to rowColor

        @Composable
        fun cell(title: String, color: Color? = null, content: @Composable () -> Unit) {
            values.add(title to Cell(color, content))
        }
    }
}

private object Table : Tag()

@Composable
private inline fun table(
    crossinline attrs: (AttrsBuilder<Table>.() -> Unit) = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope<HTMLTableElement>.() -> Unit
) {
    TagElement(
        tagName = "table",
        applyAttrs = attrs,
        applyStyle = style,
        content = content
    )
}

private object TR : Tag()

@Composable
private inline fun tr(
    crossinline attrs: (AttrsBuilder<TR>.() -> Unit) = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope<HTMLTableRowElement>.() -> Unit
) {
    TagElement(
        tagName = "tr",
        applyAttrs = attrs,
        applyStyle = style,
        content = content
    )
}

private object THEAD : Tag()

@Composable
private inline fun thead(
    crossinline attrs: (AttrsBuilder<THEAD>.() -> Unit) = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope<HTMLTableSectionElement>.() -> Unit
) {
    TagElement(
        tagName = "thead",
        applyAttrs = attrs,
        applyStyle = style,
        content = content
    )
}

private object TH : Tag()

@Composable
private inline fun th(
    crossinline attrs: (AttrsBuilder<TH>.() -> Unit) = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope<HTMLTableColElement>.() -> Unit
) {
    TagElement(
        tagName = "th",
        applyAttrs = attrs,
        applyStyle = style,
        content = content
    )
}

private object TD : Tag()

@Composable
private inline fun td(
    crossinline attrs: (AttrsBuilder<TD>.() -> Unit) = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope<HTMLTableCellElement>.() -> Unit
) {
    TagElement(
        tagName = "td",
        applyAttrs = attrs,
        applyStyle = style,
        content = content
    )
}

private object TBODY : Tag()

@Composable
private inline fun tbody(
    crossinline attrs: (AttrsBuilder<TBODY>.() -> Unit) = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope<HTMLTableSectionElement>.() -> Unit
) {
    TagElement(
        tagName = "tbody",
        applyAttrs = attrs,
        applyStyle = style,
        content = content
    )
}

@Composable
fun <T> Table(
    data: List<T>,
    color: Color? = null,
    striped: Boolean = false,
    hover: Boolean = false,
    map: @Composable Row.Builder.(T) -> Unit
) {
    val headers = mutableListOf<String>()
    val rows = mutableListOf<Row>()

    data.forEach {
        val row = Row.Builder().apply { map(it) }
        val (rowValues, rowColor) = row.build()
        val cells = rowValues.map { (header, cellValue) ->
            if (header !in headers) {
                headers.add(header)
            }
            cellValue
        }
        val row1 = Row(rowColor, cells)
        rows.add(row1)
    }
    Table(
        color = color,
        striped = striped,
        hover = hover,
        headers = headers,
        rows = rows
    )
}

@Composable
fun Table(
    color: Color? = null,
    striped: Boolean = false,
    hover: Boolean = false,
    headers: List<String>,
    rows: List<Row>
) {
    table(attrs = {
        classes {
            +"table"
            color?.let { +"table-$it" }
            if (hover) {
                +"table-hover"
            }
            if (striped) {
                +"table-striped"
            }
        }
    }) {
        thead {
            tr {
                for (header in headers) {
                    th(attrs = {
                        attr("scope", "col")
                    }) {
                        Text(header)
                    }
                }
            }
        }
        tbody {
            for (row in rows) {
                tr(attrs = {
                    classes {
                        row.color?.let { +"table-$it" }
                    }
                }) {
                    for (cell in row.cells) {
                        td(attrs = {
                            classes {
                                cell.color?.let { +"table-$it" }
                            }
                        }) {
                            cell.content()
                        }
                    }
                }
            }
        }
    }
}
