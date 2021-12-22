package app.softwork.composetodo

import androidx.compose.runtime.*
import app.softwork.composetodo.views.*

@Composable
fun MainView(appContainer: AppContainer) {
    Theme {
        Column {
            val _api by appContainer.api.collectAsState()
            when (val api = _api) {
                is API.LoggedIn -> Todos(appContainer.todoViewModel(api))
                is API.LoggedOut -> {
                    Row {
                        Login(appContainer.loginViewModel(api))
                        Register(appContainer.registerViewModel(api))
                    }
                }
            }
        }
    }
}
