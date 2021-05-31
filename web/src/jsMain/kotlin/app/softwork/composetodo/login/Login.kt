package app.softwork.composetodo.login

import androidx.compose.runtime.*
import androidx.compose.web.attributes.*
import androidx.compose.web.elements.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.*
import kotlinx.coroutines.*

@Composable
fun Login(api: API.LoggedOut, onLogin: (API.LoggedIn) -> Unit) {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    Row {
        Column {
            H1 {
                Text("Login")
            }
            Input(value = username, placeholder = "user.name", label = "Username") {
                username = it.value
            }
            Input(InputType.Password, placeholder = "password", label = "Passwort", value = password) {
                password = it.value
            }
            Button("Login", attrs = {
                disabled(username.isEmpty() || password.isEmpty())
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
