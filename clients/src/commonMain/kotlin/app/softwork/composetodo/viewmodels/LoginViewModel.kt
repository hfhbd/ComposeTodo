package app.softwork.composetodo.viewmodels

import app.softwork.composetodo.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class LoginViewModel(
    private val scope: CoroutineScope,
    private val api: API.LoggedOut,
    private val onLogin: (API.LoggedIn) -> Unit
) {
    val userName = MutableStateFlow("")
    val password = MutableStateFlow("")
    val error = MutableStateFlow<LoginResult.Failure?>(null)

    fun silentLogin() {
        scope.launch {
            api.silentLogin()
        }
    }

    fun login() {
        scope.launch {
            val loginResult = try {
                val success = api.login(username = userName.value, password = password.value)
                if (success != null) {
                    LoginResult.Success(success)
                } else {
                    LoginResult.WrongCredentials
                }
            } catch (e: IOException) {
                LoginResult.NoNetwork
            }

            when (loginResult) {
                is LoginResult.Success -> {
                    error.value = null
                    onLogin(loginResult.login)
                }
                is LoginResult.Failure -> error.value = loginResult
            }
        }
    }

    sealed class LoginResult {
        data class Success(val login: API.LoggedIn) : LoginResult()
        abstract class Failure: LoginResult() {
            abstract val reason: String
        }

        object WrongCredentials : Failure() {
            override val reason: String get() = "Wrong credentials"
        }
        object NoNetwork : Failure() {
            override val reason: String get() = "Server not available"
        }
    }
}
