package app.softwork.composetodo

import app.softwork.composetodo.viewmodels.*
import io.ktor.client.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.flow.*

interface AppContainer {
    fun todoViewModel(api: API.LoggedIn): TodoViewModel
    fun loginViewModel(api: API.LoggedOut): LoginViewModel
    fun registerViewModel(api: API.LoggedOut): RegisterViewModel

    val client: HttpClient

    suspend fun logout() {
        when (val login = api.value) {
            is API.LoggedIn -> {
                try {
                    login.logout()
                } catch (_: IOException) {
                }
                api.value = API.LoggedOut(client)
            }
            is API.LoggedOut -> {}
        }
    }

    val api: MutableStateFlow<API>
}
