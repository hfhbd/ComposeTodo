package app.softwork.composetodo

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import app.softwork.composetodo.views.Login
import app.softwork.composetodo.views.PressMeButton
import app.softwork.composetodo.views.Todos

@Composable
fun MainView(appContainer: AppContainer) {
    Row {
        PressMeButton(appContainer.pressMeViewModel)
        Login(appContainer.loginViewModel) {
            Todos(appContainer.todoViewModel)
        }
    }
}
