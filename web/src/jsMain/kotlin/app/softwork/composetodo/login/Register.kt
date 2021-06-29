package app.softwork.composetodo.login

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import app.softwork.composetodo.*
import app.softwork.composetodo.dto.*
import kotlinx.coroutines.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*

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
            Input(value = username, placeholder = "user.name", label = "Username", type = InputType.Text) { value, _ ->
                username = value
            }
            Input(
                type = InputType.Password,
                placeholder = "password",
                label = "Passwort",
                value = password
            ) { value, _ ->
                password = value
            }
            Input(
                type = InputType.Password,
                placeholder = "passwordAgain",
                label = "Passwort Again",
                value = passwordAgain
            ) { value, _ ->
                passwordAgain = value
            }
            Input(placeholder = "John", label = "First Name", value = firstName, type = InputType.Text) { value, _ ->
                firstName = value
            }
            Input(placeholder = "Doe", label = "Last Name", value = lastName, type = InputType.Text) { value, _ ->
                lastName = value
            }
            Button("Register", attrs = {
                if (password != passwordAgain || listOf(
                        username,
                        password,
                        passwordAgain,
                        firstName,
                        lastName
                    ).any { it.isEmpty() }
                ) {
                    disabled()
                }
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
