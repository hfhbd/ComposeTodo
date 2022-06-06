package app.softwork.composetodo.login

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.*
import app.softwork.composetodo.viewmodels.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*

@Composable
fun Register(viewModel: RegisterViewModel) {
    Row {
        Column {
            H1 {
                Text("Register")
            }

            val username by viewModel.username.collectAsState()
            Input(value = username, placeholder = "user.name", label = "Username", type = InputType.Text) {
                viewModel.username.value = it.value
            }

            val password by viewModel.password.collectAsState()
            Input(
                type = InputType.Password,
                placeholder = "password",
                label = "Password",
                autocomplete = AutoComplete.newPassword,
                value = password
            ) {
                viewModel.password.value = it.value
            }

            val passwordAgain by viewModel.passwordAgain.collectAsState()
            Input(
                type = InputType.Password,
                placeholder = "passwordAgain",
                label = "Password Again",
                autocomplete = AutoComplete.newPassword,
                value = passwordAgain
            ) {
                viewModel.passwordAgain.value = it.value
            }

            val firstName by viewModel.firstName.collectAsState()
            Input(placeholder = "John", label = "First Name", value = firstName, type = InputType.Text) {
                viewModel.firstName.value = it.value
            }

            val lastName by viewModel.lastName.collectAsState()
            Input(
                placeholder = "Doe",
                label = "Last Name",
                value = lastName,
                type = InputType.Text,
                autocomplete = AutoComplete.familyName
            ) {
                viewModel.lastName.value = it.value
            }

            val enableRegisterButton by viewModel.enableRegisterButton.collectAsState(false)

            Button(title = "Register", disabled = !enableRegisterButton) {
                viewModel.register()
            }

            val error = viewModel.error.collectAsState().value
            if (error != null) {
                Alert(color = Color.Danger) {
                    Text(error.reason)
                    CloseButton {
                        viewModel.error.value = null
                    }
                }
            }
        }
    }
}
