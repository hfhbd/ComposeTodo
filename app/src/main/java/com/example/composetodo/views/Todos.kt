package com.example.composetodo.views

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import com.example.composetodo.viewmodels.TodoViewModel

@Composable
fun Todos(viewModel: TodoViewModel = TodoViewModel()) {
    Column {
        Button(viewModel::refresh) { Text("Refresh") }
        viewModel.todos.forEach { todo ->
            Text(todo.title)
        }
    }
}
