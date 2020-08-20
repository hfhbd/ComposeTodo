package com.example.composetodo.utils

import android.Manifest
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

data class HttpClient(val baseURL: String, val json: Json = Json) {

    @RequiresPermission(Manifest.permission.INTERNET)
    suspend fun <T> get(url: String = "/", serializer: KSerializer<T>) =
        request(url, {
            requestMethod = "GET"
            doInput = true
            doOutput = false
        }) {
            json.decodeFromString(serializer, inputStream.reader().readText())
        }

    @RequiresPermission(Manifest.permission.INTERNET)
    suspend fun<T, R> post(url: String = "/", body: T, bodySerializer: KSerializer<T>, serializer: KSerializer<R>) = request(url, {
        requestMethod = "POST"
        doInput = true
        doOutput = true
        outputStream.writer().write(json.encodeToString(bodySerializer, body))
    }) { json.decodeFromString(serializer, inputStream.reader().readText()) }

    @RequiresPermission(Manifest.permission.INTERNET)
    suspend fun<T, R> patch(url: String = "/", body: T, bodySerializer: KSerializer<T>, serializer: KSerializer<R>) = request(url, {
        requestMethod = "PATCH"
        doInput = true
        doOutput = true
        outputStream.writer().write(json.encodeToString(bodySerializer, body))
    }) { json.decodeFromString(serializer, inputStream.reader().readText()) }

    @RequiresPermission(Manifest.permission.INTERNET)
    suspend fun<T, R> put(url: String = "/", body: T, bodySerializer: KSerializer<T>, serializer: KSerializer<R>) = request(url, {
        requestMethod = "PUT"
        doInput = true
        doOutput = true
        outputStream.writer().write(json.encodeToString(bodySerializer, body))
    }) { json.decodeFromString(serializer, inputStream.reader().readText()) }

    @RequiresPermission(Manifest.permission.INTERNET)
    suspend fun delete(url: String = "/") = request(url, {
        requestMethod = "DELETE"
        doInput = false
        doOutput = false
    }) { Unit }

    private suspend fun <T> request(
        url: String,
        beforeConnection: HttpsURLConnection.() -> Unit,
        afterConnection: HttpsURLConnection.() -> T
    ) = withContext(Dispatchers.IO) {
        val connection = URL(baseURL + url).openConnection() as HttpsURLConnection
        try {
            connection.run {
                beforeConnection()
                connect()
                if (status in HttpStatus.validStatus) {
                    afterConnection()
                } else {
                    null
                }
            }
        } catch (e: IOException) {
            null
        } finally {
            connection.disconnect()
        }
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
