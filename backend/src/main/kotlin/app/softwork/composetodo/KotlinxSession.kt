package app.softwork.composetodo

import io.ktor.server.sessions.*
import kotlinx.serialization.*

fun <T> KSerializer<T>.toSessionSerializer(format: StringFormat) = object : SessionSerializer<T> {
    override fun deserialize(text: String): T = format.decodeFromString(this@toSessionSerializer, text)
    override fun serialize(session: T): String = format.encodeToString(this@toSessionSerializer, session)
}

inline fun <reified T : Any> SessionsConfig.cookie(
    name: String,
    serializer: KSerializer<T>,
    format: StringFormat,
    block: CookieConfiguration.() -> Unit
) {
    val configuration = CookieConfiguration().apply(block)
    register(
        provider = SessionProvider(
            name = name,
            type = T::class,
            tracker = SessionTrackerByValue(T::class, serializer = serializer.toSessionSerializer(format)),
            transport = SessionTransportCookie(name, configuration = configuration, transformers = emptyList())
        )
    )
}
