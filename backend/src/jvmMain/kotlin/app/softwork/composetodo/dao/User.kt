package app.softwork.composetodo.dao

import app.softwork.composetodo.definitions.*
import io.ktor.auth.*
import kotlinx.uuid.*
import kotlinx.uuid.exposed.*
import org.jetbrains.exposed.dao.id.*

// TODO wait for Exposed DTO support
class User(id: EntityID<UUID>) : KotlinxUUIDEntity(id), Principal {
    companion object : KotlinxUUIDEntityClass<User>(Users)

    var username by Users.name
    var password by Users.password

    var firstName by Users.firstName
    var lastName by Users.lastName

    val todos by Todo referrersOn Todos.user
}
