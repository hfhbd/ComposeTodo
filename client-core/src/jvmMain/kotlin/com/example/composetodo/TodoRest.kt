package com.example.composetodo

import com.example.composetodo.dto.Todo
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.datetime.Instant
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

interface TodoRest {
    suspend fun getAll(): List<Todo>?
    suspend fun getModified(after: Instant): List<Todo>?
    suspend fun update(todo: Todo): Todo?
    suspend fun delete(todo: Todo): Unit?

    companion object {
        fun todoRest(
            client: HttpClient = HttpClient(Android) {
                defaultRequest {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = "jsonplaceholder.typicode.com"
                        encodedPath = "/todos$encodedPath"
                    }
                }
            }, json: Json = Json
        ) =
            object : TodoRest {

                override suspend fun getAll(): List<Todo> = client.get<String>("/").let {
                    json.decodeFromString(ListSerializer(Todo.serializer()), it)
                }

                override suspend fun getModified(after: Instant): List<Todo> =
                    client.get<String>("/?after=$after").let {
                        json.decodeFromString(ListSerializer(Todo.serializer()), it)
                    }


                override suspend fun update(todo: Todo) =
                    client.put<String>("/") {
                        body = json.encodeToString(Todo.serializer(), todo)
                    }.let {
                        json.decodeFromString(Todo.serializer(), it)
                    }

                override suspend fun delete(todo: Todo) = client.delete<Unit>("/${todo.id}")
            }

    }
}