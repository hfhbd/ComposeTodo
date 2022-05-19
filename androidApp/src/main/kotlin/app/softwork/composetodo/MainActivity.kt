package app.softwork.composetodo

import android.os.*
import androidx.activity.*
import androidx.activity.compose.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = Container(applicationContext)

        setContent {
            MainView(appContainer)
        }
    }
}
