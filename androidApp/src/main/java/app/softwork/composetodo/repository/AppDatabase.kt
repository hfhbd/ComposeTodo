package app.softwork.composetodo.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.softwork.composetodo.models.Todo
import app.softwork.composetodo.repository.converter.KotlinxUUIDConverter
import app.softwork.composetodo.repository.converter.LocalDateTimeConverter

@Database(entities = [Todo::class], version = 1)
@TypeConverters(KotlinxUUIDConverter::class, LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val todoDao: TodoDao

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "appDB").build()
    }
}

