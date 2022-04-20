package app.softwork.composetodo.viewmodels

import app.softwork.composetodo.*
import app.softwork.composetodo.dto.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class RegisterViewModel(
    val api: API.LoggedOut,
    private val onLogin: (API.LoggedIn) -> Unit
) : ViewModel() {
    val username = MutableStateFlow("")
    val password = MutableStateFlow("")
    val passwordAgain = MutableStateFlow("")
    val firstName = MutableStateFlow("")
    val lastName = MutableStateFlow("")

    val error = MutableStateFlow<Failure?>(null)

    val enableRegisterButton = combine(password, passwordAgain, username) { password, again, userName ->
        userName.isNotEmpty() && password.isNotEmpty() && password == again
    }

    fun register() {
        val newUser = User.New(
            username = username.value,
            password = password.value,
            passwordAgain = passwordAgain.value,
            firstName = firstName.value,
            lastName = lastName.value
        )
        lifecycleScope.launch {
            api.networkCall(action = {
                register(newUser)
            }, onSuccess = {
                error.value = null
                onLogin(it)
            }) {
                error.value = it
            }
        }
    }
}

suspend fun API.LoggedOut.networkCall(
    action: suspend API.LoggedOut.() -> API.LoggedIn?,
    onSuccess: (API.LoggedIn) -> Unit,
    onFailure: (Failure) -> Unit
) {
    try {
        val success = action()
        if (success != null) {
            onSuccess(success)
        } else {
            onFailure(Failure.WrongCredentials)
        }
    } catch (e: IOException) {
        println(e)
        onFailure(Failure.NoNetwork)
    }
}
