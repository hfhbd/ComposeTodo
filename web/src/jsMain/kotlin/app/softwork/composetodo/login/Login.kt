package app.softwork.composetodo.login

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.viewmodels.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*

@Composable
fun Login(viewModel: LoginViewModel) {
    Row {
        Column {
            val username by viewModel.userName.collectAsState()
            H1 {
                Text("Login with Username $username")
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
                label = "Password",
                autocomplete = AutoComplete.currentPassword,
                value = password
            ) {
                viewModel.password.value = it.value
            }
            val enableLogin by viewModel.enableLogin.collectAsState(false)

            Button(title = "Login $username", disabled = !enableLogin) {
                viewModel.login()
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
