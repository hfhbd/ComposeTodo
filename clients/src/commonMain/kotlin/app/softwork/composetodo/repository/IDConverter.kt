package app.softwork.composetodo.repository

import app.cash.sqldelight.*
import app.softwork.composetodo.dto.*
import app.softwork.uuid.*
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object IDConverter : ColumnAdapter<TodoDTO.ID, String> {
    override fun decode(databaseValue: String) = TodoDTO.ID(databaseValue.toUuid())
    override fun encode(value: TodoDTO.ID) = value.id.toString()
}
