package app.softwork.composetodo.login

import androidx.compose.runtime.*
import androidx.compose.web.attributes.*
import app.softwork.composetodo.*
import kotlinx.coroutines.*

@Composable
fun Login(api: API.LoggedOut, onLogin: (API.LoggedIn) -> Unit) {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    Row {
        Column {
            input(value = username, placeholder = "user.name", label = "Username") {
                username = it.value
            }
            input(InputType.Password, placeholder = "password", label = "Passwort", value = password) {
                password = it.value
            }
            if (username.isNotEmpty() && password.isNotEmpty()) {
                button("Login") {
                    scope.launch {
                        api.login(username = username, password = password)?.let {
                            onLogin(it)
                        }
                    }
                }
            }
        }
    }
}
