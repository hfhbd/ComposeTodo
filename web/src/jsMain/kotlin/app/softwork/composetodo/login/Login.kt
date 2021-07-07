package app.softwork.composetodo.login

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*

@Composable
fun Login(api: API.LoggedOut, onLogin: (API.LoggedIn) -> Unit) {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    Row {
        Column {
            H1 {
                Text("Login")
            }
            Input(
                value = username,
                placeholder = "user.name",
                label = "Username",
                autocomplete = AutoComplete.username,
                type = InputType.Text
            ) {
                username = it.value
            }
            Input(
                type = InputType.Password,
                placeholder = "password",
                label = "Passwort",
                autocomplete = AutoComplete.currentPassword,
                value = password
            ) {
                password = it.value
            }
            Button("Login", attrs = {
                if (username.isEmpty() || password.isEmpty()) {
                    disabled()
                }
            }) {
                scope.launch {
                    api.login(username = username, password = password)?.let {
                        onLogin(it)
                    }
                }
            }
        }
    }
}
