package com.example.composetodo.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

data class Todo(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
) {
    companion object {
        fun serializer() = object : KSerializer<Todo> {
            override val descriptor: SerialDescriptor
                get() = TODO("Not yet implemented")

            override fun deserialize(decoder: Decoder): Todo {
                TODO("Not yet implemented")
            }

            override fun serialize(encoder: Encoder, value: Todo) {
                TODO("Not yet implemented")
            }
        }
    }
}
