package app.softwork.composetodo

import androidx.compose.foundation.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.*
import kotlinx.datetime.*

@Composable
fun Button(title: String, enabled: Boolean, onClick: () -> Unit) {
    Button(onClick, enabled = enabled) {
        Text(title)
    }
}

@Composable
fun TextField(
    label: String,
    placeholder: String,
    value: String,
    isPassword: Boolean,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun H6(value: String) {
    Text(value, style = MaterialTheme.typography.h6)
}

@Composable
fun Theme(content: @Composable () -> Unit) {
    MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
        content()
    }
}

@Composable
expect fun DateField(value: Instant?, label: String, onValueChange: (Instant?) -> Unit)
