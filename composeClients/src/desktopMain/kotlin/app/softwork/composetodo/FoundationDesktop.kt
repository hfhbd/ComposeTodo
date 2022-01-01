package app.softwork.composetodo

import androidx.compose.foundation.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.*
import kotlinx.datetime.*

@Composable
actual fun Column(content: @Composable () -> Unit) {
    androidx.compose.foundation.layout.Column { content() }
}

@Composable
actual fun Row(content: @Composable () -> Unit) {
    androidx.compose.foundation.layout.Row { content() }
}

@Composable
actual fun Card(content: @Composable () -> Unit) {
    androidx.compose.material.Card { content() }
}

@Composable
actual fun Text(value: String) {
    androidx.compose.material.Text(value)
}

@Composable
actual fun Button(title: String, enabled: Boolean, onClick: () -> Unit) {
    androidx.compose.material.Button(onClick, enabled = enabled) {
        Text(title)
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
    androidx.compose.material.TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
actual fun H6(value: String) {
    androidx.compose.material.Text(value, style = typography.h6)
}

@Composable
actual fun Theme(content: @Composable () -> Unit) {
    MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
        content()
    }
}

@Composable
actual fun DateField(value: Instant?, label: String, onValueChange: (Instant?) -> Unit) {
    val initValue = remember { Clock.System.now() }
    TextField(value = (value ?: initValue).toString(), label = { Text(label) }, onValueChange = {
        onValueChange(it.toInstantOrNull())
    })
}

public fun String.toInstantOrNull(): Instant? = try {
    Instant.parse(this)
} catch (cause: IllegalArgumentException) {
    null
}
