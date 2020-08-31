package com.example.composetodo.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.ClassSerialDescriptorBuilder
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.reflect.KProperty1

@Entity
data class Todo(
    @PrimaryKey val id: Int,
    val title: String,
    val completed: Boolean
) {
    companion object {
        fun serializer() = object : KSerializer<Todo> {
            override val descriptor = buildClassSerialDescriptor("TodoSerializer") {
                element(Todo::id)
                element("userId", Int.serializer().descriptor) // not used
                element(Todo::title)
                element(Todo::completed)
            }

            override fun deserialize(decoder: Decoder): Todo {
                val dec = decoder.beginStructure(descriptor)
                var id: Int? = null
                var title: String? = null
                var completed: Boolean? = null
                loop@ while (true) {
                    when (val i = dec.decodeElementIndex(descriptor)) {
                        CompositeDecoder.DECODE_DONE -> break@loop
                        0 -> id = dec.decodeIntElement(descriptor, i)
                        1 -> { dec.decodeIntElement(descriptor, i) }
                        2 -> title = dec.decodeStringElement(descriptor, i)
                        3 -> completed = dec.decodeBooleanElement(descriptor, i)
                        else -> throw SerializationException("Unknown index $i")
                    }
                }
                dec.endStructure(descriptor)
                return Todo(
                    id = id ?: throw SerializationException("missing id"),
                    title = title ?: throw SerializationException("missing title"),
                    completed = completed ?: throw SerializationException("missing completed")
                )
            }

            override fun serialize(encoder: Encoder, value: Todo) {
                val compositeOutput = encoder.beginStructure(descriptor)
                compositeOutput.encodeIntElement(descriptor, 0, value.id)
                compositeOutput.encodeStringElement(descriptor, 2, value.title)
                compositeOutput.encodeBooleanElement(descriptor, 3, value.completed)
                compositeOutput.endStructure(descriptor)
            }
        }
    }
}

@JvmName("elementInt")
internal fun <T> ClassSerialDescriptorBuilder.element(elementName: KProperty1<T, Int>) =
    element(elementName.name, Int.serializer().descriptor)

@JvmName("elementBoolean")
internal fun <T> ClassSerialDescriptorBuilder.element(elementName: KProperty1<T, Boolean>) =
    element(elementName.name, Boolean.serializer().descriptor)

@JvmName("elementString")
internal fun <T> ClassSerialDescriptorBuilder.element(elementName: KProperty1<T, String>) =
    element(elementName.name, String.serializer().descriptor)
