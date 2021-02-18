package app.softwork.composetodo.repository.converter

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime

class LocalDateTimeConverter {
    @TypeConverter
    fun fromISOString(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }

    @TypeConverter
    fun dateToISOString(date: LocalDateTime?): String? = date?.toString()
}