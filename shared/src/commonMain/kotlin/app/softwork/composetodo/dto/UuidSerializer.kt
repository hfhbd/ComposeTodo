package app.softwork.composetodo.dto

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// Copied from kotlinx-serialization until a new version is out
@OptIn(ExperimentalUuidApi::class)
public object UuidSerializer : KSerializer<Uuid> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("kotlin.uuid.Uuid", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Uuid) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Uuid {
        return Uuid.parse(decoder.decodeString())
    }
}
