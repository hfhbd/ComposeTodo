package app.softwork.composetodo

import androidx.compose.runtime.*
import com.vanpra.composematerialdialogs.*
import com.vanpra.composematerialdialogs.datetime.date.*
import kotlinx.datetime.*
import kotlinx.datetime.Instant
import java.time.LocalDate

@Composable
actual fun DateField(value: Instant?, label: String, onValueChange: (Instant?) -> Unit) {
    val dialog = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialog,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        datepicker { date: LocalDate ->
            onValueChange(date.toKotlinLocalDate().atStartOfDayIn(TimeZone.currentSystemDefault()))
        }
    }
    Button(value?.toString() ?: "Select Date", enabled = true) {
        dialog.show()
    }
}
