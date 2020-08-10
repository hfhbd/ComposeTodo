package com.example.composetodo.views

import androidx.compose.foundation.Text
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import com.example.composetodo.viewmodels.PressMeViewModel

@Composable
fun PressMeButton(viewModel: PressMeViewModel = PressMeViewModel()) {
    Button(viewModel::loadNew) {
        Text(viewModel.text)
    }
}
