package app.softwork.composetodo

import androidx.compose.runtime.*
import kotlinx.datetime.*

@Composable
expect fun Column(content: @Composable () -> Unit)

@Composable
expect fun Row(content: @Composable () -> Unit)

@Composable
expect fun Card(content: @Composable () -> Unit)

@Composable
expect fun Text(value: String)

@Composable
expect fun Button(title: String, enabled: Boolean, onClick: () -> Unit)

@Composable
expect fun TextField(
    label: String,
    placeholder: String,
    value: String,
    isPassword: Boolean,
    onValueChange: (String) -> Unit,
)

@Composable
expect fun H6(value: String)

@Composable
expect fun Theme(content: @Composable () -> Unit)

@Composable
expect fun DateField(value: Instant?, label: String, onValueChange: (Instant?) -> Unit)
