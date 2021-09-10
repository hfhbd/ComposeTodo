package app.softwork.composetodo.views

import androidx.compose.runtime.*
import app.softwork.composetodo.*
import app.softwork.composetodo.viewmodels.*

@Composable
fun Register(viewModel: RegisterViewModel) {
    Column {
        val userName by viewModel.username.collectAsState()
        TextField(
            label = "Username",
            value = userName,
            onValueChange = { viewModel.username.value = it },
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

        val passwordAgain by viewModel.passwordAgain.collectAsState()
        TextField(
            label = "Password Again",
            value = password,
            onValueChange = { viewModel.passwordAgain.value = it },
            isPassword = true,
            placeholder = ""
        )

        val firstName by viewModel.firstName.collectAsState()
        TextField(
            label = "First Name",
            value = password,
            onValueChange = { viewModel.firstName.value = it },
            isPassword = false,
            placeholder = ""
        )

        val lastName by viewModel.lastName.collectAsState()
        TextField(
            label = "Last Name",
            value = password,
            onValueChange = { viewModel.lastName.value = it },
            isPassword = false,
            placeholder = ""
        )

        val enabled by viewModel.enableRegisterButton.collectAsState(false)
        Button("Register", enabled = enabled) { viewModel.register() }
    }
}
