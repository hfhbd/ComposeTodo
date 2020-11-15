package com.example.composetodo.repository

import androidx.room.*
import com.example.composetodo.models.Todo

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo")
    suspend fun getAll(): List<Todo>

    @Query("SELECT * FROM todo WHERE completed = :isCompleted")
    suspend fun getAll(isCompleted: Boolean): List<Todo>

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

    @Query("DELETE from todo")
    suspend fun deleteAll()
}
