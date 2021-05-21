package app.softwork.composetodo.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.*
import app.softwork.composetodo.*
import app.softwork.composetodo.viewmodels.*

@Composable
fun Login(viewModel: LoginViewModel, onSuccess: @Composable (API.LoggedIn) -> Unit) {
    val login = viewModel.login
    if (login != null) {
        onSuccess(login)
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