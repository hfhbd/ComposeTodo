package app.softwork.composetodo.viewmodels

import app.softwork.composetodo.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class LoginViewModel(
    private val api: API.LoggedOut,
    private val onLogin: (API.LoggedIn) -> Unit
) : ViewModel() {
    val userName = MutableStateFlow("")
    val password = MutableStateFlow("")

    val error = MutableStateFlow<Failure?>(null)

    val enableLogin = userName.combine(password) { userName, password ->
        userName.isNotEmpty() && password.isNotEmpty()
    }

    fun login() {
        error.value = null
        lifecycleScope.launch {
            api.networkCall(
                action = {
                    login(username = userName.value, password = password.value)
                },
                onSuccess = {
                    error.value = null
                    onLogin(it)
                }
            ) {
                error.value = it
            }
        }
    }
}
