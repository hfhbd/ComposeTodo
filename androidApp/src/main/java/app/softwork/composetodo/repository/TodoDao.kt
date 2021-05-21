package app.softwork.composetodo.repository

import androidx.room.*
import app.softwork.composetodo.models.Todo

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo")
    suspend fun getAll(): List<Todo>

    @Query("SELECT * FROM todo WHERE finished = :isFinished")
    suspend fun getAll(isFinished: Boolean): List<Todo>

    @Update
    suspend fun update(todo: Todo)

    @Update
    suspend fun update(todo: List<Todo>)

    @Insert
    suspend fun insert(todos: List<Todo>)

    @Insert
    suspend fun insert(todos: Todo)

    @Delete
    suspend fun delete(todo: List<Todo>)

    @Delete
    suspend fun delete(todo: Todo)
}
