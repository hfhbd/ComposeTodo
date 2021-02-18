package app.softwork.composetodo.controller

import app.softwork.composetodo.dao.User
import app.softwork.composetodo.toDTO
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object AdminController {
    suspend fun allUsers() = newSuspendedTransaction {
        User.all().map { it.toDTO() }
    }
}