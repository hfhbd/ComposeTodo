package app.softwork.composetodo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Row
import androidx.lifecycle.lifecycleScope
import app.softwork.composetodo.repository.AppDatabase
import app.softwork.composetodo.repository.TodoRepository
import app.softwork.composetodo.viewmodels.LoginViewModel
import app.softwork.composetodo.viewmodels.PressMeViewModel
import app.softwork.composetodo.viewmodels.TodoViewModel
import app.softwork.composetodo.views.Login
import app.softwork.composetodo.views.PressMeButton
import app.softwork.composetodo.views.Todos

class MainActivity : AppCompatActivity() {
    lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = Container()

        setContent {
            Row {
                PressMeButton(appContainer.pressMeViewModel)
                Login(appContainer.loginViewModel) {
                    Todos(appContainer.todoViewModel)
                }
            }
        }
    }

    inner class Container: AppContainer {
        private val db = AppDatabase.getInstance(applicationContext)
        override val loginViewModel = LoginViewModel(lifecycleScope, api = api)
        override val todoViewModel = TodoViewModel(lifecycleScope, TodoRepository(db.todoDao))
        override val pressMeViewModel = PressMeViewModel(lifecycleScope)
    }
}
