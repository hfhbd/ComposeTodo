package com.example.myapplicationresult

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.setContent
import kotlinx.coroutines.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Row {
                PressMeButton()
            }
        }
    }
}

class TodoViewModel {
    var todos by mutableStateOf(emptyList<Todo>())
    fun loadNew() {
        GlobalScope.launch(Dispatchers.IO) {
            val newTodos = getNewTodos()
            withContext(Dispatchers.Main) {
                todos = newTodos
            }
        }
    }

    private suspend fun deleteTodo(id: Int) = delete("").getOrDefault(Unit)

    private suspend fun getNewTodos() =
        get("https://jsonplaceholder.typicode.com/todos", ListSerializer(Todo.serializer())).getOrDefault(emptyList())

    suspend fun <T: Any> get(url: String, serializer: KSerializer<T>) = request(url, HTTPMethod.GET(serializer))

    suspend fun delete(url: String) = request(url, HTTPMethod.DELETE)

    suspend fun <T: Any> request(url: String, method: HTTPMethod<T>) = withContext(Dispatchers.IO) {
        val connection = URL(url).openConnection() as HttpsURLConnection
        val result = try {
            connection.run {
                method.beforeConnection(this)
                connect()
                Result.success(method.afterConnection(this))
            }
        } catch (e: IOException) {
            Result.failure(e)
        } finally {
            connection.disconnect()
        }
        result
    }
}

sealed class HTTPMethod<T>(val beforeConnection: (HttpsURLConnection) -> Unit, val afterConnection: (HttpsURLConnection) -> T) {
    companion object {
        val json = Json
    }
    class GET<T>(serializer: KSerializer<T>) : HTTPMethod<T>({
        it.requestMethod = "GET"
        it.doInput = true
    }, {
        json.decodeFromString(serializer, it.inputStream.reader().readText())
    })
    data class POST<In: Any, Out: Any>(val out: Out, val outKSerializer: KSerializer<Out>, val inSerializer: KSerializer<In>) : HTTPMethod<In>({
        it.requestMethod = "POST"
        it.doInput = true
        it.doOutput = true
        it.outputStream.writer().write(json.encodeToString(outKSerializer, out))
    }, {
        json.decodeFromString(inSerializer, it.inputStream.reader().readText())
    })
    data class PUT(val out: String) : HTTPMethod("PUT", true, true)
    object DELETE : HTTPMethod<Unit>({
        it.requestMethod = "DELETE"
    }, {

    })
}

class PressMeViewModel {

    var text by mutableStateOf("Press Me")

    fun loadNew() {
        GlobalScope.launch(Dispatchers.IO) {
            val newText = testing()
            withContext(Dispatchers.Main) {
                text = newText
            }
        }
    }

    suspend fun testing() = newInt().fold(onFailure = {
        "Error: ${it.message!!}"
    }, onSuccess = {
        it.toString()
    })

    @OptIn(ExperimentalTime::class)
    suspend fun newInt(): Result<Int> {
        delay(5.seconds)
        return if (Boolean.random()) {
            Result.success(42)
        } else {
            Result.failure(IllegalStateException("Blubb"))
        }
    }

    fun Boolean.Companion.random(): Boolean {
        return (0..1).random().toBoolean()
    }

    fun Int.toBoolean() = when (this) {
        1 -> true
        else -> false
    }
}

@Composable
fun PressMeButton() {
    val viewModel = PressMeViewModel()
    Button(onClick = {
        viewModel.loadNew()
    }) {
        Text(viewModel.text)
    }
}
