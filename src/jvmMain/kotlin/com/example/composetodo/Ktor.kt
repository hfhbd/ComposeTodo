package com.example.composetodo

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.uuid.UUID
import kotlin.reflect.KProperty

suspend fun <T> ApplicationCall.respondJson(serializer: KSerializer<T>, data: T, json: Json = Json) =
    respondText(contentType = ContentType.Application.Json) {
        json.encodeToString(serializer, data)
    }

suspend fun <T> ApplicationCall.respondJson(serializer: KSerializer<T>, data: List<T>, json: Json = Json) =
    respondText(contentType = ContentType.Application.Json) {
        json.encodeToString(ListSerializer(serializer), data)
    }

suspend fun <T> ApplicationCall.body(serializer: KSerializer<T>, json: Json = Json) =
    json.decodeFromString(serializer, receiveText())

operator fun Parameters.getValue(receiver: Any?, property: KProperty<*>): UUID =
    UUID(getOrFail(property.name))
