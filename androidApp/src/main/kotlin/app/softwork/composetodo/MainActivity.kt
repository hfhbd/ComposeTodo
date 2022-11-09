package app.softwork.composetodo

import android.os.*
import androidx.activity.*
import androidx.activity.compose.*
import app.cash.sqldelight.driver.android.*
import app.softwork.composetodo.repository.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

class MainActivity : ComponentActivity() {

    private val client = HttpClient(Android) {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.todo.softwork.app"
            }
        }
        install(Resources)
        install(ContentNegotiation) {
            json()
        }
    }

    private val db = TodoRepository.createDatabase(
        AndroidSqliteDriver(
            ComposeTodoDB.Schema,
            applicationContext,
            "composetodo.db"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = AppContainer(client, db)

        setContent {
            MainView(appContainer)
        }
    }
}
