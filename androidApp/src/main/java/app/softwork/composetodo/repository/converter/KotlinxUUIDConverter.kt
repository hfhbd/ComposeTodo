package app.softwork.composetodo.repository.converter

import androidx.room.TypeConverter
import kotlinx.uuid.UUID

class KotlinxUUIDConverter {
    @TypeConverter
    fun fromUUIDString(value: String?): UUID? = value?.let { UUID(it) }

    @TypeConverter
    fun uuidToString(uuid: UUID?): String? = uuid?.toString()
}