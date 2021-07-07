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
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordAgain by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    Row {
        Column {
            H1 {
                Text("Register")
            }
            Input(value = username, placeholder = "user.name", label = "Username", type = InputType.Text) {
                username = it.value
            }
            Input(
                type = InputType.Password,
                placeholder = "password",
                label = "Passwort",
                autocomplete = AutoComplete.newPassword,
                value = password
            ) {
                password = it.value
            }
            Input(
                type = InputType.Password,
                placeholder = "passwordAgain",
                label = "Passwort Again",
                autocomplete = AutoComplete.newPassword,
                value = passwordAgain
            ) {
                passwordAgain = it.value
            }
            Input(placeholder = "John", label = "First Name", value = firstName, type = InputType.Text) {
                firstName = it.value
            }
            Input(
                placeholder = "Doe",
                label = "Last Name",
                value = lastName,
                type = InputType.Text,
                autocomplete = AutoComplete.familyName
            ) {
                lastName = it.value
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
