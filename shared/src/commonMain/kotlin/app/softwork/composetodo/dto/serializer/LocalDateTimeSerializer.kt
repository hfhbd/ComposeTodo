package app.softwork.composetodo.dto.serializer

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LocalDateTimeSerializer: KSerializer<LocalDateTime> {
    override val descriptor = PrimitiveSerialDescriptor("LocalDateSerializer", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) =
        decoder.decodeString().toLocalDateTime()

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.toString())
    }
}
