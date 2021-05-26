package app.softwork.composetodo.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import app.softwork.composetodo.models.*

@Composable
fun TodoRow(todo: Todo) {
    Card {
        Row {
            Column {
                Text(todo.title, style = typography.h6)
            }
        }
    }
}