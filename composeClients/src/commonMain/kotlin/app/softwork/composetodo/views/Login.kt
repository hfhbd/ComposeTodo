package app.softwork.composetodo.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import app.softwork.composetodo.*
import app.softwork.composetodo.viewmodels.*

@Composable
fun Login(viewModel: LoginViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val state by remember(viewModel, coroutineScope) { viewModel.state(coroutineScope) }.collectAsState()

    Login(
        username = state.username,
        updateUsername = viewModel::updateUsername,
        password = state.password,
        updatePassword = viewModel::updatePassword,
        enableLogin = state.enableLogin,
        onLoginClick = viewModel::login,
        error = state.error
    )
}

@Composable
private fun Login(
    username: String,
    updateUsername: (String) -> Unit,
    password: String,
    updatePassword: (String) -> Unit,
    enableLogin: Boolean,
    onLoginClick: () -> Unit,
    error: Failure?
) {
    Column {
        TextField(
            label = "Username",
            value = username,
            onValueChange = updateUsername,
            isPassword = false,
            placeholder = "John Doe"
        )
        TextField(
            label = "Password",
            value = password,
            onValueChange = updatePassword,
            isPassword = true,
            placeholder = ""
        )

        Button("Login", enabled = enableLogin) { onLoginClick() }

        if (error != null) {
            Text("ERROR: ${error.reason}")
        }
    }
}
