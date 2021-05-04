package app.softwork.composetodo.controller

import app.softwork.composetodo.*
import app.softwork.composetodo.dao.User
import app.softwork.composetodo.definitions.*
import app.softwork.composetodo.dto.*
import kotlinx.uuid.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.*
import kotlin.time.*

class UserController(val user: User) {

    companion object {
        @ExperimentalTime
        suspend fun createUser(
            jwtProvider: JWTProvider,
            newUser: app.softwork.composetodo.dto.User.New
        ): Token {
            require(newUser.password == newUser.passwordAgain)
            val user = newSuspendedTransaction {
                User.new {
                    username = newUser.username
                    password = newUser.password
                    firstName = newUser.firstName
                    lastName = newUser.lastName
                }
            }
            return jwtProvider.token(user)
        }

        suspend fun find(userID: UUID) = newSuspendedTransaction {
            User.findById(userID)
        }

        suspend fun findBy(name: String, password: String) = newSuspendedTransaction {
            User.find {
                Users.name eq name and (Users.password eq password)
            }.firstOrNull()
        }
    }

    suspend fun delete() = newSuspendedTransaction {
        user.delete()
    }

    suspend fun update(toUpdate: app.softwork.composetodo.dto.User) =
        newSuspendedTransaction {
            user.apply {
                firstName = toUpdate.firstName
                lastName = toUpdate.lastName
            }.toDTO()
        }
}
