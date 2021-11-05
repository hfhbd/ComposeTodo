package app.softwork.composetodo.viewmodels

import app.softwork.composetodo.*
import app.softwork.composetodo.dto.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class RegisterViewModel(
    val scope: CoroutineScope,
    val api: API.LoggedOut,
    private val onLogin: (API.LoggedIn) -> Unit
) {
    val username = MutableStateFlow("")
    val password = MutableStateFlow("")
    val passwordAgain = MutableStateFlow("")
    val firstName = MutableStateFlow("")
    val lastName = MutableStateFlow("")

    val enableRegisterButton = password.combine(passwordAgain) { password, again ->
        password == again
    }

    fun register() {
        val newUser = User.New(
            username = username.value,
            password = password.value,
            passwordAgain = passwordAgain.value,
            firstName = firstName.value,
            lastName = lastName.value
        )
        scope.launch {
            onLogin(api.register(newUser))
        }
    }
}
