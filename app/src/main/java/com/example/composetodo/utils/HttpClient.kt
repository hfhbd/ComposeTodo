package com.example.composetodo.utils

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

@WorkerThread
data class HttpClient(val baseURL: String = "", val json: Json = Json) {

    @RequiresPermission(Manifest.permission.INTERNET)
    suspend fun <T : Any> get(url: String = "/", serializer: KSerializer<T>) =
        request(url, HTTPMethod.GET)?.let {
            json.decodeFromString(serializer, it)
        }

    @RequiresPermission(Manifest.permission.INTERNET)
    suspend fun delete(url: String = "/") = request(url, HTTPMethod.DELETE).let { Unit }

    private suspend fun request(url: String, method: HTTPMethod) = withContext(Dispatchers.IO) {
        val connection = URL(baseURL + url).openConnection() as HttpsURLConnection
        try {
            connection.run {
                method.beforeConnection(this)
                connect()
                if (status !in HttpStatus.validStatus) {
                    throw IOException("Not valid status: $status")
                } else {
                    return@withContext inputStream.reader().readText()
                }
            }
        } catch (e: IOException) {
            return@withContext null
        } finally {
            connection.disconnect()
        }
    }

    private sealed class HTTPMethod(
        val beforeConnection: (HttpsURLConnection) -> Unit
    ) {

        object GET : HTTPMethod({
            it.requestMethod = "GET"
            it.doInput = true
            it.doOutput = false
        })

        data class POST(val body: String) : HTTPMethod({
            it.requestMethod = "POST"
            it.doInput = true
            it.doOutput = true
            it.outputStream.writer().write(body)
        })

        data class PUT(val body: String) : HTTPMethod({
            it.requestMethod = "PUT"
            it.doInput = true
            it.doOutput = true
            it.outputStream.writer().write(body)
        })

        data class PATCH(val body: String) : HTTPMethod({
            it.requestMethod = "PATCH"
            it.doInput = true
            it.doOutput = true
            it.outputStream.writer().write(body)
        })

        object DELETE : HTTPMethod({
            it.requestMethod = "DELETE"
            it.doInput = false
            it.doOutput = false
        })
    }

    enum class HttpStatus(val code: Int) {
        OK(200);

        companion object {
            val validStatus = listOf(OK)
        }
    }

    private val HttpsURLConnection.status: HttpStatus
        get() = HttpStatus.values().first { it.code == this.responseCode }
}
