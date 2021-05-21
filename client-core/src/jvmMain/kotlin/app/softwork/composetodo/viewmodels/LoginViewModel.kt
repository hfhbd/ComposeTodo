package app.softwork.composetodo.viewmodels

import androidx.compose.runtime.*
import app.softwork.composetodo.*
import kotlinx.coroutines.*

class LoginViewModel(private val scope: CoroutineScope, private val api: API.LoggedOut) {
    var userName by mutableStateOf("")
    var password by mutableStateOf("")
    var login: API.LoggedIn? by mutableStateOf(null)

    fun login() {
        scope.launch {
            login = api.login(username = userName, password = password)
        }
    }
}