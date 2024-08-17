package app.softwork.composetodo.dto

import kotlinx.datetime.*
import kotlinx.serialization.*
import kotlin.jvm.*
import kotlin.uuid.*

@Serializable
public data class TodoDTO(
    val id: ID,
    val title: String,
    val until: Instant?,
    val finished: Boolean,
    val recordChangeTag: String?
) {
    @Serializable
    @JvmInline
    public value class ID(
        @Serializable(with = UuidSerializer::class)
        @OptIn(ExperimentalUuidApi::class)
        public val id: Uuid,
    )
}
