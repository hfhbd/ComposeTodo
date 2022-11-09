package app.softwork.composetodo.viewmodels

import androidx.compose.runtime.*
import app.cash.molecule.*
import app.softwork.composetodo.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class LoginViewModel(
    private val api: API.LoggedOut,
    private val onLogin: (API.LoggedIn) -> Unit
) : ViewModel() {
    data class LoginState(
        val username: String,
        val password: String,
        val enableLogin: Boolean,
        val error: Failure?
    )

    private var username by mutableStateOf("")
    fun updateUsername(new: String) {
        username = new
    }

    private var password by mutableStateOf("")
    fun updatePassword(new: String) {
        password = new
    }

    private var error by mutableStateOf<Failure?>(null)
    fun dismissError() {
        error = null
    }

    fun state(
        coroutineScope: CoroutineScope,
        clock: RecompositionClock = RecompositionClock.ContextClock
    ): StateFlow<LoginState> = coroutineScope.launchMolecule(clock) {
        val isError = username.isNotEmpty() && password.isNotEmpty()

        LoginState(
            username = username,
            password = password,
            enableLogin = isError,
            error = error
        )
    }

    fun login() {
        error = null
        lifecycleScope.launch {
            api.networkCall(
                action = {
                    login(username = username, password = password)
                }, onSuccess = {
                    error = null
                    onLogin(it)
                }
            ) {
                error = it
            }
        }
    }
}
