package app.softwork.composetodo

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.*

@DslMarker
annotation class TypedAPI

@TypedAPI
inline fun <reified T : Any, reified Body : Any, reified Return : Any> Route.post(
    crossinline action: suspend RoutingContext.(T, Body) -> Return
): Route = resource<T> {
    method(HttpMethod.Post) {
        val serializer = serializer<T>()
        handle(serializer) {
            val body: Body = call.receive()
            call.respond(action(it, body))
        }
    }
}

@TypedAPI
inline fun <reified T : Any, reified Return : Any> Route.post(
    crossinline action: suspend RoutingContext.(T) -> Return
): Route = resource<T> {
    method(HttpMethod.Post) {
        val serializer = serializer<T>()
        handle(serializer) {
            call.respond(action(it))
        }
    }
}

@TypedAPI
inline fun <reified T : Any, reified Body : Any, reified Return : Any> Route.put(
    crossinline action: suspend RoutingContext.(T, Body) -> Return
): Route = resource<T> {
    method(HttpMethod.Put) {
        val serializer = serializer<T>()
        handle(serializer) {
            val body: Body = call.receive()
            call.respond(action(it, body))
        }
    }
}

@TypedAPI
inline fun <reified T : Any, reified Return : Any> Route.get(
    crossinline action: suspend RoutingContext.(T) -> Return
): Route = resource<T> {
    method(HttpMethod.Get) {
        val serializer = serializer<T>()
        handle(serializer) {
            call.respond(action(it))
        }
    }
}

@TypedAPI
inline fun <reified T : Any, reified Return : Any> Route.delete(
    crossinline action: suspend RoutingContext.(T) -> Return
): Route = resource<T> {
    method(HttpMethod.Delete) {
        val serializer = serializer<T>()
        handle(serializer) {
            call.respond(action(it))
        }
    }
}

val RoutingContext.user get() = call.principal<app.softwork.composetodo.dao.User>()!!
