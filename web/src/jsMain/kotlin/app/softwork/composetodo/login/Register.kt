package app.softwork.composetodo.login

import androidx.compose.runtime.*
import androidx.compose.web.attributes.*
import androidx.compose.web.elements.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.*
import app.softwork.composetodo.dto.*
import kotlinx.coroutines.*

@Composable
fun Register(api: API.LoggedOut, onLogin: (API.LoggedIn) -> Unit) {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordAgain by mutableStateOf("")
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")

    Row {
        Column {
            H1 {
                Text("Register")
            }
            Input(value = username, placeholder = "user.name", label = "Username") {
                username = it.value
            }
            Input(InputType.Password, placeholder = "password", label = "Passwort", value = password) {
                password = it.value
            }
            Input(InputType.Password, placeholder = "passwordAgain", label = "Passwort Again", value = passwordAgain) {
                passwordAgain = it.value
            }
            Input(placeholder = "John", label = "First Name", value = firstName) {
                firstName = it.value
            }
            Input(placeholder = "Doe", label = "Last Name", value = lastName) {
                lastName = it.value
            }
            Button("Register", attrs = {
                disabled(password != passwordAgain || listOf(
                    username,
                    password,
                    passwordAgain,
                    firstName,
                    lastName
                ).any { it.isEmpty() })
            }) {
                scope.launch {
                    onLogin(
                        api.register(
                            User.New(
                                username = username,
                                password = password,
                                passwordAgain = passwordAgain,
                                firstName = firstName,
                                lastName = lastName
                            )
                        )
                    )
                }
            }
        }
    }
}
