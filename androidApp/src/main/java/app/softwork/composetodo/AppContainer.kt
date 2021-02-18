package app.softwork.composetodo

import app.softwork.composetodo.viewmodels.LoginViewModel
import app.softwork.composetodo.viewmodels.PressMeViewModel
import app.softwork.composetodo.viewmodels.TodoViewModel

interface AppContainer {
    val todoViewModel: TodoViewModel
    val pressMeViewModel: PressMeViewModel
    val loginViewModel: LoginViewModel
}
