package app.softwork.composetodo.login

import androidx.compose.runtime.*
import androidx.compose.web.attributes.*
import androidx.compose.web.elements.*
import androidx.compose.web.elements.Text
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
            input(value = username, placeholder = "user.name", label = "Username") {
                username = it.value
            }
            input(InputType.Password, placeholder = "password", label = "Passwort", value = password) {
                password = it.value
            }
            input(InputType.Password, placeholder = "passwordAgain", label = "Passwort Again", value = passwordAgain) {
                passwordAgain = it.value
            }
            input(placeholder = "John", label = "First Name", value = firstName) {
                firstName = it.value
            }
            input(placeholder = "Doe", label = "Last Name", value = lastName) {
                lastName = it.value
            }
            if (listOf(
                    username,
                    password,
                    passwordAgain,
                    firstName,
                    lastName
                ).all { it.isNotEmpty() } && password == passwordAgain
            ) {
                button("Register") {
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
}
