package app.softwork.composetodo.repository

import app.cash.sqldelight.*
import app.softwork.composetodo.dto.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
object IDConverter : ColumnAdapter<TodoDTO.ID, String> {
    override fun decode(databaseValue: String) = TodoDTO.ID(Uuid.parse(databaseValue))
    override fun encode(value: TodoDTO.ID) = value.id.toString()
}
