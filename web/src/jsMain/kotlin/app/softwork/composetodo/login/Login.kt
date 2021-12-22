package app.softwork.composetodo.login

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.*
import app.softwork.composetodo.viewmodels.*
import kotlinx.coroutines.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*

@Composable
fun Login(viewModel: LoginViewModel) {
    Row {
        Column {
            val username by viewModel.userName.collectAsState()
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
                viewModel.userName.value = it.value
            }
            val password by viewModel.password.collectAsState()
            Input(
                type = InputType.Password,
                placeholder = "password",
                label = "Passwort",
                autocomplete = AutoComplete.currentPassword,
                value = password
            ) {
                viewModel.password.value = it.value
            }
            val enableLogin by viewModel.enableLogin.collectAsState(false)

            Button("Login $username", disabled = !enableLogin) {
                viewModel.login()
            }

            val error by viewModel.error.collectAsState()
            error?.let {
                Alert(color = Color.Danger) {
                    Text(it.reason)
                    CloseButton {
                        viewModel.error.value = null
                    }
                }
            }
        }
    }
}
