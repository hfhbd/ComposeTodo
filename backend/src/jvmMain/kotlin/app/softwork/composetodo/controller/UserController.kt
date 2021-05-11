package app.softwork.composetodo.controller

import app.softwork.composetodo.*
import app.softwork.composetodo.dao.User
import app.softwork.composetodo.definitions.*
import app.softwork.composetodo.dto.*
import kotlinx.uuid.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.*
import kotlin.time.*

class UserController(private val db: Database) {
    @ExperimentalTime
    suspend fun createUser(
        jwtProvider: JWTProvider,
        newUser: app.softwork.composetodo.dto.User.New
    ): Token {
        require(newUser.password == newUser.passwordAgain)
        val user = newSuspendedTransaction(db = db) {
            User.new {
                username = newUser.username
                password = newUser.password
                firstName = newUser.firstName
                lastName = newUser.lastName
            }
        }
        return jwtProvider.token(user)
    }

    suspend fun find(userID: UUID) = newSuspendedTransaction(db = db) {
        User.findById(userID)
    }

    suspend fun findBy(name: String, password: String) = newSuspendedTransaction(db = db) {
        User.find {
            Users.name eq name and (Users.password eq password)
        }.firstOrNull()
    }


    suspend fun delete(user: User) = newSuspendedTransaction(db = db) {
        user.delete()
    }

    suspend fun update(user: User, toUpdate: app.softwork.composetodo.dto.User) =
        newSuspendedTransaction(db = db) {
            user.apply {
                firstName = toUpdate.firstName
                lastName = toUpdate.lastName
            }.toDTO()
        }
}
