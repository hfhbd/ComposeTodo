package app.softwork.composetodo

import app.softwork.composetodo.viewmodels.*
import com.squareup.sqldelight.db.*
import io.ktor.client.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.flow.*

interface AppContainer {
    val driver: SqlDriver

    fun todoViewModel(api: API.LoggedIn): TodoViewModel
    fun loginViewModel(api: API.LoggedOut): LoginViewModel
    fun registerViewModel(api: API.LoggedOut): RegisterViewModel

    val client: HttpClient

    suspend fun logout() {
        when (val login = isLoggedIn.value) {
            is API.LoggedIn -> {
                try { login.logout() } catch (e: IOException) { }
                isLoggedIn.value = API.LoggedOut(client)
            }
            is API.LoggedOut -> { }
        }
    }

    val isLoggedIn: MutableStateFlow<API>
}
