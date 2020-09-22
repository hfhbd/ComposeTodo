package com.example.composetodo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

interface HttpClient {
    suspend fun <T> get(url: String = "/", serializer: KSerializer<T>): T?
    suspend fun <T, R> post(
        url: String = "/",
        body: T,
        bodySerializer: KSerializer<T>,
        serializer: KSerializer<R>
    ): R?

    suspend fun <T, R> patch(
        url: String = "/",
        body: T,
        bodySerializer: KSerializer<T>,
        serializer: KSerializer<R>
    ): R?

    suspend fun <T, R> put(url: String = "/", body: T, bodySerializer: KSerializer<T>, serializer: KSerializer<R>): R?
    suspend fun delete(url: String = "/"): Unit?

    companion object {
        fun httpClient(baseURL: String, json: Json = Json) = object : HttpClient {

            override suspend fun <T> get(url: String, serializer: KSerializer<T>) =
                request(url, {
                    requestMethod = "GET"
                    doInput = true
                    doOutput = false
                }) {
                    json.decodeFromString(serializer, inputStream.reader().readText())
                }

            override suspend fun <T, R> post(
                url: String,
                body: T,
                bodySerializer: KSerializer<T>,
                serializer: KSerializer<R>
            ) = request(url, {
                requestMethod = "POST"
                doInput = true
                doOutput = true
                outputStream.writer().write(json.encodeToString(bodySerializer, body))
            }) { json.decodeFromString(serializer, inputStream.reader().readText()) }

            override suspend fun <T, R> patch(
                url: String,
                body: T,
                bodySerializer: KSerializer<T>,
                serializer: KSerializer<R>
            ) = request(url, {
                requestMethod = "PATCH"
                doInput = true
                doOutput = true
                outputStream.writer().write(json.encodeToString(bodySerializer, body))
            }) { json.decodeFromString(serializer, inputStream.reader().readText()) }

            override suspend fun <T, R> put(
                url: String,
                body: T,
                bodySerializer: KSerializer<T>,
                serializer: KSerializer<R>
            ) =
                request(url, {
                    requestMethod = "PUT"
                    doInput = true
                    doOutput = true
                    outputStream.writer().write(json.encodeToString(bodySerializer, body))
                }) { json.decodeFromString(serializer, inputStream.reader().readText()) }

            override suspend fun delete(url: String) = request(url, {
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
                        withContext(Dispatchers.Default) { beforeConnection() }
                        connect()
                        if (responseCode in 200..300) {
                            withContext(Dispatchers.Default) { afterConnection() }
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
        }
    }
}
