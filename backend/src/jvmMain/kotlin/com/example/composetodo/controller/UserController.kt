package com.example.composetodo.controller

import com.example.composetodo.dao.User
import com.example.composetodo.toDTO
import kotlinx.uuid.UUID
import kotlinx.uuid.toJavaUUID
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserController(userID: UUID) {
    private val user = suspend { User[userID.toJavaUUID()] }

    companion object {
        suspend fun allUsers() = newSuspendedTransaction {
            User.all().map { it.toDTO() }
        }
        suspend fun createUser(newUser: com.example.composetodo.dto.User) = newSuspendedTransaction {
            User.new(newUser.id.toJavaUUID()) {
                firstName = newUser.firstName
                lastName = newUser.lastName
            }.toDTO()
        }
    }

    suspend fun getUser() = newSuspendedTransaction { user().toDTO() }

    suspend fun delete() = newSuspendedTransaction {
        user().delete()
    }

    suspend fun update(toUpdate: com.example.composetodo.dto.User) =
        newSuspendedTransaction {
            user().apply {
                firstName = toUpdate.firstName
                lastName = toUpdate.lastName
            }.toDTO()
        }
}