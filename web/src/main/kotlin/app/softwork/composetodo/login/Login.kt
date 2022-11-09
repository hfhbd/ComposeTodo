package app.softwork.composetodo.login

import androidx.compose.runtime.*
import app.cash.molecule.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.viewmodels.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*

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
        error = state.error,
        dismissError = viewModel::dismissError
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
    error: Failure?,
    dismissError: () -> Unit
) {
    Row {
        Column {
            H1 {
                Text("Login $username")
            }
            Input(
                value = username,
                placeholder = "user.name",
                label = "Username",
                autocomplete = AutoComplete.username,
                type = InputType.Text
            ) {
                updateUsername(it.value)
            }

            Input(
                type = InputType.Password,
                placeholder = "password",
                label = "Password",
                autocomplete = AutoComplete.currentPassword,
                value = password
            ) {
                updatePassword(it.value)
            }

            Button(title = "Login $username", disabled = !enableLogin) {
                onLoginClick()
            }

            if (error != null) {
                Alert(color = Color.Danger) {
                    Text(error.reason)
                    CloseButton {
                        dismissError()
                    }
                }
            }
        }
    }
}
