package app.softwork.composetodo.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import app.softwork.composetodo.*

@Composable
fun TodoRow(todo: Todo) {
    Card {
        Row {
            Column {
                H6(todo.title)
            }
        }
    }
}
