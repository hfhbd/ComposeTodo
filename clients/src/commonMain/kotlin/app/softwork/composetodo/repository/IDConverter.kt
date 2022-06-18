package app.softwork.composetodo.repository

import app.softwork.composetodo.dto.*
import app.cash.sqldelight.*
import kotlinx.uuid.sqldelight.*

object IDConverter : ColumnAdapter<TodoDTO.ID, String> {
    override fun decode(databaseValue: String) = TodoDTO.ID(UUIDStringAdapter.decode(databaseValue))
    override fun encode(value: TodoDTO.ID) = UUIDStringAdapter.encode(value.id)
}
