package app.softwork.composetodo.controller

import app.softwork.cloudkitclient.*
import app.softwork.composetodo.*
import app.softwork.composetodo.dao.*

class AdminController(private val db: Client.Database) {
    suspend fun allUsers() = db.query(User).map { it.toDTO() }
}
