package app.softwork.composetodo

import androidx.compose.runtime.*
import kotlinx.datetime.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*

@Composable
actual fun Column(content: @Composable () -> Unit) {
    app.softwork.bootstrapcompose.Column { content() }
}

@Composable
actual fun Row(content: @Composable () -> Unit) {
    app.softwork.bootstrapcompose.Row { content() }
}

@Composable
actual fun Card(content: @Composable () -> Unit) {
    app.softwork.bootstrapcompose.Card { content() }
}

@Composable
actual fun Text(value: String) {
    org.jetbrains.compose.web.dom.Text(value)
}

@Composable
actual fun Button(title: String, enabled: Boolean, onClick: () -> Unit) {
    app.softwork.bootstrapcompose.Button(title, disabled = !enabled) {
        onClick()
    }
}

@Composable
actual fun TextField(
    label: String,
    placeholder: String,
    value: String,
    isPassword: Boolean,
    onValueChange: (String) -> Unit,
) {
    val type: InputType<String> = if (isPassword) InputType.Password else InputType.Text
    app.softwork.bootstrapcompose.Input(
        label = label,
        placeholder = placeholder,
        value = value,
        type = type
    ) {
        onValueChange(it.value)
    }
}

@Composable
actual fun H6(value: String) {
    H6 { Text(value) }
}

@Composable
actual fun Theme(content: @Composable () -> Unit) {
    content()
}

@Composable
actual fun DateField(value: Instant?, label: String, onValueChange: (Instant?) -> Unit) {
    app.softwork.bootstrapcompose.Input(
        type = InputType.DateTimeLocal,
        label = label,
        value = value?.toString() ?: ""
    ) {
        onValueChange(it.value.toInstant())
    }
}
