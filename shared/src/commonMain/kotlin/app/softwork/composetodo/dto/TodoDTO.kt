package app.softwork.composetodo.dto

import kotlinx.datetime.*
import kotlinx.serialization.*
import kotlinx.uuid.*
import kotlin.jvm.*

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
    public value class ID(public val id: UUID)
}
