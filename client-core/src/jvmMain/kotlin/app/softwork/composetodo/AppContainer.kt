package app.softwork.composetodo

import app.softwork.composetodo.viewmodels.*

interface AppContainer {
    fun todoViewModel(api: API.LoggedIn): TodoViewModel
    val pressMeViewModel: PressMeViewModel
    val loginViewModel: LoginViewModel
}
