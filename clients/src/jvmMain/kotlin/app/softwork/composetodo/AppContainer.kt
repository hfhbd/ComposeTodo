package app.softwork.composetodo

import app.softwork.composetodo.models.*
import app.softwork.composetodo.viewmodels.*

interface AppContainer<T: Todo> {
    fun todoViewModel(api: API.LoggedIn): TodoViewModel<T>
    val pressMeViewModel: PressMeViewModel
    val loginViewModel: LoginViewModel
}
