package app.softwork.composetodo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import app.softwork.composetodo.repository.AppDatabase
import app.softwork.composetodo.repository.TodoRepo
import app.softwork.composetodo.repository.TodoRepository
import app.softwork.composetodo.viewmodels.LoginViewModel
import app.softwork.composetodo.viewmodels.PressMeViewModel
import app.softwork.composetodo.viewmodels.TodoViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var appContainer: AppContainer<TodoEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = Container()

        setContent {
            MainView(appContainer)
        }
    }

    inner class Container : AppContainer<TodoEntity> {
        private val api = API.LoggedOut(HttpClient(Android) {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.todo.softwork.app"
                }
            }
        })
        private val db = AppDatabase.getInstance(applicationContext)
        override val loginViewModel = LoginViewModel(lifecycleScope, api = api)
        override fun todoViewModel(api: API.LoggedIn) =
            TodoViewModel(lifecycleScope, TodoRepo(db.todoDao, api = api))
        override val pressMeViewModel = PressMeViewModel(lifecycleScope)
    }
}
