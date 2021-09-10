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
    var userName = MutableStateFlow("")
    var password = MutableStateFlow("")
    var error = MutableStateFlow<LoginResult.Failure?>(null)

    fun silentLogin() {
        scope.launch {
            api.silentLogin()
        }
    }

    fun login() {
        scope.launch {
            val loginResult: LoginResult = try {
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
        abstract class Failure: LoginResult()

        object WrongCredentials : Failure()
        object NoNetwork : Failure()
    }
}
