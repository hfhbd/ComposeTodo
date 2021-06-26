package app.softwork.composetodo.controller

import app.softwork.cloudkitclient.*
import app.softwork.composetodo.*
import app.softwork.composetodo.dao.User
import app.softwork.composetodo.dto.*
import kotlin.time.*

class UserController(private val db: Client.Database) {
    @ExperimentalTime
    suspend fun createUser(
        jwtProvider: JWTProvider,
        newUser: User
    ): Token {
        val user = db.create(newUser, User)

        return jwtProvider.token(RefreshToken(user.recordName))
    }

    suspend fun find(username: String): User? = db.read(username, User)

    suspend fun findBy(name: String, password: String) = db.read(name, User)?.takeIf {
        it.fields.password?.value == password
    }

    suspend fun delete(user: User) = db.delete(user, User)
    suspend fun update(user: User) = db.update(user, User)
}
