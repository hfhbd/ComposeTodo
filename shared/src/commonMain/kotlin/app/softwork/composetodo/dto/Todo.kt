package app.softwork.composetodo.dto

import app.softwork.composetodo.*
import kotlinx.datetime.*
import kotlinx.serialization.*
import kotlinx.uuid.*

@Serializable
data class Todo(
    override val id: UUID,
    val title: String,
    val until: Instant?,
    val finished: Boolean,
    val recordChangeTag: String?
) : Identifiable
