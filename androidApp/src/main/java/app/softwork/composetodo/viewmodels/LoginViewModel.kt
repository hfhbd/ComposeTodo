package app.softwork.composetodo.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.softwork.composetodo.API
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LoginViewModel(private val scope: CoroutineScope, private val api: API) {
    var userName by mutableStateOf("")
    var password by mutableStateOf("")
    var success by mutableStateOf(false)

    fun login() {
        scope.launch {
            success = api.login(userName = userName, password = password)
        }
    }
}