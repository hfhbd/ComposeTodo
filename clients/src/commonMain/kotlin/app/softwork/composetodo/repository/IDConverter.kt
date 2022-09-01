package app.softwork.composetodo.repository

import app.cash.sqldelight.*
import app.softwork.composetodo.dto.*
import kotlinx.uuid.*

object IDConverter : ColumnAdapter<TodoDTO.ID, String> {
    override fun decode(databaseValue: String) = TodoDTO.ID(databaseValue.toUUID())
    override fun encode(value: TodoDTO.ID) = value.id.toString()
}
