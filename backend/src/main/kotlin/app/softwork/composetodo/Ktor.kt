package app.softwork.composetodo

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.*
import kotlinx.uuid.*
import kotlin.reflect.*

suspend fun <T> ApplicationCall.respondJson(
    serializer: KSerializer<T>,
    json: Json = Json,
    data: suspend ApplicationCall.() -> T
) =
    respondText(contentType = ContentType.Application.Json) {
        json.encodeToString(serializer, data())
    }

suspend fun <T> ApplicationCall.respondJsonList(
    serializer: KSerializer<T>,
    json: Json = Json,
    data: suspend ApplicationCall.() -> List<T>
) =
    respondText(contentType = ContentType.Application.Json) {
        json.encodeToString(ListSerializer(serializer), data())
    }

suspend fun <T> ApplicationCall.body(serializer: KSerializer<T>, json: Json = Json) =
    json.decodeFromString(serializer, receiveText())

operator fun Parameters.getValue(receiver: Any?, property: KProperty<*>): UUID =
    UUID(getOrFail(property.name))

val PipelineContext<Unit, ApplicationCall>.user get() = call.principal<app.softwork.composetodo.dao.User>()!!
