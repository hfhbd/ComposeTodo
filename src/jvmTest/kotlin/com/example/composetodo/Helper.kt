package com.example.composetodo

import io.ktor.server.testing.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlin.test.assertEquals

internal fun <T> TestApplicationResponse.assertEqualsJsonBody(
    expected: T,
    serializer: KSerializer<T>, json: Json = Json
) {
    assertEquals(expected, json.decodeFromString(serializer, content!!))
}

internal fun <T> TestApplicationResponse.assertEqualsJsonBody(
    expected: List<T>,
    serializer: KSerializer<T>, json: Json = Json
) {
    assertEquals(expected, json.decodeFromString(ListSerializer(serializer), content!!))
}

internal fun <T> TestApplicationRequest.setBody(
    value: T,
    serializer: KSerializer<T>, json: Json = Json
) {
    setBody(json.encodeToString(serializer, value))
}