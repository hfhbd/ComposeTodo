package com.example.composetodo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

data class HttpClient(val baseURL: String = "", val json: Json = Json) {
    suspend fun <T : Any> get(url: String = "/", serializer: KSerializer<T>) = request(url, HTTPMethod.GET).mapCatching {
        json.decodeFromString(serializer, it)
    }

    suspend fun delete(url: String = "/") = request(url, HTTPMethod.DELETE).mapCatching { Unit }

    private suspend fun request(url: String, method: HTTPMethod): Result<String> = withContext(Dispatchers.IO) {
        val connection = URL(baseURL + url).openConnection() as HttpsURLConnection
        try {
            connection.run {
                method.beforeConnection(this)
                connect()
                if (status !in HttpStatus.validStatus) {
                    throw IOException("Not valid status: $status")
                } else {
                    return@withContext Result.success(inputStream.reader().readText())
                }
            }
        } catch (e: IOException) {
            return@withContext Result.failure(e)
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
        companion object { val validStatus = listOf(OK) }
    }

    private val HttpsURLConnection.status: HttpStatus
        get() = HttpStatus.values().first { it.code == this.responseCode }
}
