package app.softwork.composetodo.repository

import androidx.room.*
import app.softwork.composetodo.models.*

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo")
    suspend fun getAll(): List<TodoEntity>

    @Query("SELECT * FROM todo WHERE finished = :isFinished")
    suspend fun getAll(isFinished: Boolean): List<TodoEntity>

    @Update
    suspend fun update(todo: TodoEntity)

    @Update
    suspend fun update(todo: List<TodoEntity>)

    @Insert
    suspend fun insert(todos: List<TodoEntity>)

    @Insert
    suspend fun insert(todos: TodoEntity)

    @Delete
    suspend fun delete(todo: List<TodoEntity>)

    @Delete
    suspend fun delete(todo: TodoEntity)
}
