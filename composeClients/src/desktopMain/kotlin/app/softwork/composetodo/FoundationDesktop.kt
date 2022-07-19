package app.softwork.composetodo

import androidx.compose.material.*
import androidx.compose.runtime.*
import kotlinx.datetime.*

@Composable
actual fun DateField(value: Instant?, label: String, onValueChange: (Instant?) -> Unit) {
    val initValue = remember { Clock.System.now() }
    TextField(value = (value ?: initValue).toString(), label = { Text(label) }, onValueChange = {
        onValueChange(it.toInstantOrNull())
    })
}

fun String.toInstantOrNull(): Instant? = try {
    Instant.parse(this)
} catch (ignored: IllegalArgumentException) {
    null
}
