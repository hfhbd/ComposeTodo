package app.softwork.composetodo.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.PasswordVisualTransformation
import app.softwork.composetodo.viewmodels.LoginViewModel

@Composable
fun Login(viewModel: LoginViewModel, onSuccess: @Composable () -> Unit) {
    if (viewModel.success) {
        onSuccess()
    } else {
        Column {
            TextField(
                value = viewModel.userName,
                onValueChange = { viewModel.userName = it }
            )
            TextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(viewModel::login) {
                Text("Login")
            }
        }
    }
}