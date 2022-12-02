package app.softwork.composetodo

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import app.softwork.composetodo.views.*

@Composable
fun MainView(appContainer: AppContainer) {
    Theme {
        Column {
            when (remember { appContainer.api }.collectAsState().value) {
                is API.LoggedIn -> Todos(appContainer.todoViewModel())
                is API.LoggedOut -> {
                    Row {
                        Login(appContainer.loginViewModel())
                        Register(appContainer.registerViewModel())
                    }
                }
            }
        }
    }
}
