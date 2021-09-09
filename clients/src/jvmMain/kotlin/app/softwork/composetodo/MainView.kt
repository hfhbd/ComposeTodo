package app.softwork.composetodo

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import app.softwork.composetodo.models.*
import app.softwork.composetodo.views.*

@Composable
fun <T : Todo> MainView(appContainer: AppContainer<T>, isDarkSystem: Boolean = isSystemInDarkTheme()) {
    MaterialTheme(colors = if (isDarkSystem) darkColors() else lightColors()) {
        Column {
            PressMeButton(appContainer.pressMeViewModel)
            Login(appContainer.loginViewModel) {
                Todos(appContainer.todoViewModel(it))
            }
        }
    }
}
