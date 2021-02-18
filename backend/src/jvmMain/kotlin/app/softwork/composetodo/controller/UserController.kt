package app.softwork.composetodo.controller

import app.softwork.composetodo.dao.User
import app.softwork.composetodo.toDTO
import kotlinx.uuid.UUID
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserController(userID: UUID) {
    private val user = suspend { User[userID] }

    companion object {
        suspend fun createUser(newUser: app.softwork.composetodo.dto.User) = newSuspendedTransaction {
            User.new(newUser.id) {
                firstName = newUser.firstName
                lastName = newUser.lastName
            }.toDTO()
        }
    }

    suspend fun getUser() = newSuspendedTransaction { user().toDTO() }

    suspend fun delete() = newSuspendedTransaction {
        user().delete()
    }

    suspend fun update(toUpdate: app.softwork.composetodo.dto.User) =
        newSuspendedTransaction {
            user().apply {
                firstName = toUpdate.firstName
                lastName = toUpdate.lastName
            }.toDTO()
        }
}