package app.softwork.composetodo.views

import androidx.compose.runtime.*
import app.softwork.composetodo.*
import app.softwork.composetodo.viewmodels.*

@Composable
fun Register(viewModel: RegisterViewModel) {
    Column {
        val userName by remember { viewModel.username }.collectAsState()
        TextField(
            label = "Username",
            value = userName,
            onValueChange = { viewModel.username.value = it },
            isPassword = false,
            placeholder = "John Doe"
        )
        val password by remember { viewModel.password }.collectAsState()
        TextField(
            label = "Password",
            value = password,
            onValueChange = { viewModel.password.value = it },
            isPassword = true,
            placeholder = ""
        )

        val passwordAgain by remember { viewModel.passwordAgain }.collectAsState()
        TextField(
            label = "Password Again",
            value = passwordAgain,
            onValueChange = { viewModel.passwordAgain.value = it },
            isPassword = true,
            placeholder = ""
        )

        val firstName by remember { viewModel.firstName }.collectAsState()
        TextField(
            label = "First Name",
            value = firstName,
            onValueChange = { viewModel.firstName.value = it },
            isPassword = false,
            placeholder = ""
        )

        val lastName by remember { viewModel.lastName }.collectAsState()
        TextField(
            label = "Last Name",
            value = lastName,
            onValueChange = { viewModel.lastName.value = it },
            isPassword = false,
            placeholder = ""
        )

        val enabled by remember { viewModel.enableRegisterButton }.collectAsState(false)
        Button("Register", enabled = enabled) { viewModel.register() }
    }
}
