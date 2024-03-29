package app.softwork.composetodo

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import app.softwork.composetodo.views.*

@Composable
fun MainView(appContainer: AppContainer) {
    Theme {
        Column {
            when (val api = remember { appContainer.api }.collectAsState().value) {
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
