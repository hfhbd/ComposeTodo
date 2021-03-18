package app.softwork.composetodo

import android.os.*
import androidx.activity.compose.*
import androidx.appcompat.app.*
import androidx.compose.foundation.layout.*
import androidx.lifecycle.*
import app.softwork.composetodo.repository.*
import app.softwork.composetodo.viewmodels.*
import app.softwork.composetodo.views.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.http.*

class MainActivity : AppCompatActivity() {
    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = Container()

        setContent {
            MainView(appContainer)
        }
    }

    inner class Container : AppContainer {
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
