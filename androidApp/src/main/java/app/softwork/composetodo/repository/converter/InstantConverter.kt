package app.softwork.composetodo.repository.converter

import androidx.room.*
import kotlinx.datetime.*

class InstantConverter {
    @TypeConverter
    fun fromMilliseconds(value: Long?): Instant? = value?.let { Instant.fromEpochMilliseconds(it) }

    @TypeConverter
    fun toMilliseconds(instant: Instant?): Long? = instant?.toEpochMilliseconds()
}