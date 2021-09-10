package app.softwork.composetodo.views

import androidx.compose.runtime.*
import app.softwork.composetodo.*
import app.softwork.composetodo.viewmodels.*

@Composable
fun Login(viewModel: LoginViewModel) {
    Column {
        val userName by viewModel.userName.collectAsState()
        TextField(
            label = "Username",
            value = userName,
            onValueChange = { viewModel.userName.value = it },
            isPassword = false,
            placeholder = "John Doe"
        )
        val password by viewModel.password.collectAsState()
        TextField(
            label = "Password",
            value = password,
            onValueChange = { viewModel.password.value = it },
            isPassword = true,
            placeholder = ""
        )
        Button("Login", enabled = true) { viewModel.login() }
    }
}
