package com.example.composetodo.todo

import com.example.composetodo.HttpClient
import com.example.composetodo.HttpClient.Companion.httpClient
import kotlinx.serialization.builtins.ListSerializer
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

interface TodoRest {
    suspend fun getAll(): List<Todo>?
    suspend fun getModified(after: OffsetDateTime): List<Todo>?
    suspend fun update(todo: Todo): Todo?
    suspend fun delete(todo: Todo): Unit?

    companion object {
        fun todoRest(client: HttpClient = httpClient("https://jsonplaceholder.typicode.com/todos")) =
            object : TodoRest {
                override suspend fun getAll(): List<Todo>? = client.get("/", ListSerializer(Todo.serializer()))

                override suspend fun getModified(after: OffsetDateTime): List<Todo>? =
                    client.get(
                        "/?after=${after.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)}",
                        ListSerializer(Todo.serializer())
                    )


                override suspend fun update(todo: Todo) =
                    client.put(body = todo, bodySerializer = Todo.serializer(), serializer = Todo.serializer())

                override suspend fun delete(todo: Todo) = client.delete("/${todo.id}")

            }
    }
}
