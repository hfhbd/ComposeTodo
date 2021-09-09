package app.softwork.composetodo.models

import kotlinx.datetime.*
import kotlinx.uuid.*

interface Todo {
    val id: UUID
    val title: String
    val until: Instant?
    val finished: Boolean
    val recordChangeTag: String?

    companion object
}
