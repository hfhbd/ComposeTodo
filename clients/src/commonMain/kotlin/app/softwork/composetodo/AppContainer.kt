package app.softwork.composetodo

import app.softwork.composetodo.repository.*
import app.softwork.composetodo.viewmodels.*
import io.ktor.client.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class AppContainer(
    val client: HttpClient,
    val db: ComposeTodoDB
) : ViewModel() {

    fun todoViewModel(): TodoViewModel {
        val api = api.value as API.LoggedIn
        return TodoViewModel(TodoRepository(api, db.todoQueries))
    }
    fun loginViewModel(): LoginViewModel {
        val api = api.value as API.LoggedOut
        return LoginViewModel(api) {
            this.api.value = it
        }
    }
    fun registerViewModel(): RegisterViewModel {
        val api = api.value as API.LoggedOut
        return RegisterViewModel(api) {
            this.api.value = it
        }
    }

    fun logout() {
        lifecycleScope.launch {
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
    }

    val api: MutableStateFlow<API> = MutableStateFlow(API.LoggedOut(client))
}
