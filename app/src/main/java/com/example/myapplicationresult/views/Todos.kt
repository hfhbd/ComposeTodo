package com.example.myapplicationresult.views

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import com.example.myapplicationresult.viewmodels.TodoViewModel

@Composable
fun Todos() {
    val viewModel = TodoViewModel()
    Column {
        Button(viewModel::loadNew) { Text("Refresh") }
        viewModel.todos.forEach { 
            Text(it.title)
        }
    }
}
