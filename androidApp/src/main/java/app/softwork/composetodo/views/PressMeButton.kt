package app.softwork.composetodo.views

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import app.softwork.composetodo.viewmodels.PressMeViewModel

@Composable
fun PressMeButton(viewModel: PressMeViewModel) {
    Button(viewModel::loadNew) {
        Text(viewModel.text)
    }
}
