package app.softwork.composetodo

import androidx.compose.runtime.*
import app.softwork.composetodo.views.*

@Composable
fun MainView(appContainer: AppContainer) {
    Theme {
        Column {
            val _isLoggedIn by appContainer.isLoggedIn.collectAsState()
            when (val isLoggedIn = _isLoggedIn) {
                is API.LoggedIn -> Todos(appContainer.todoViewModel(isLoggedIn))
                is API.LoggedOut -> {
                    Row {
                        Login(appContainer.loginViewModel(isLoggedIn))
                        Register(appContainer.registerViewModel(isLoggedIn))
                    }
                }
            }
        }
    }
}
