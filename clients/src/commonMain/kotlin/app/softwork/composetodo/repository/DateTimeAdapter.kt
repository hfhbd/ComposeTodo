package app.softwork.composetodo.repository

import app.cash.sqldelight.*
import kotlinx.datetime.*

object DateTimeAdapter : ColumnAdapter<Instant, String> {
    override fun decode(databaseValue: String): Instant = Instant.parse(databaseValue)
    override fun encode(value: Instant): String = value.toString()
}
