package app.softwork.composetodo.dto

import io.ktor.util.*
import kotlinx.datetime.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

@Serializable
data class Token(val content: String) {

    val payload: Payload by lazy {
        val (_, payloadPart, _) = content.split(".")
        @OptIn(InternalAPI::class)
        val payloadJson = payloadPart.decodeBase64String()
        Json.decodeFromString(Payload.serializer(), payloadJson)
    }

    @Serializable
    data class Payload(
        @SerialName("iss") val issuer: String,
        @SerialName("sub") val subject: String,
        @Serializable(with = InstantSerializer::class) @SerialName("exp") val expiredAt: Instant,
        @Serializable(with = InstantSerializer::class) @SerialName("nbf") val notBefore: Instant,
        @Serializable(with = InstantSerializer::class) @SerialName("iat") val issuedAt: Instant,
        @SerialName("aud") val audience: String
    )

    object InstantSerializer : KSerializer<Instant> {
        override val descriptor = PrimitiveSerialDescriptor("InstantSerializer", PrimitiveKind.LONG)

        override fun deserialize(decoder: Decoder) =
            Instant.fromEpochSeconds(decoder.decodeLong())

        override fun serialize(encoder: Encoder, value: Instant) {
            encoder.encodeLong(value.epochSeconds)
        }
    }
}
