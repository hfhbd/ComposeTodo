package com.example.myapplicationresult.views

import androidx.compose.foundation.Text
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import com.example.myapplicationresult.viewmodels.PressMeViewModel

@Composable
fun PressMeButton() {
    val viewModel = PressMeViewModel()
    Button(onClick = {
        viewModel.loadNew()
    }) {
        Text(viewModel.text)
    }
}
