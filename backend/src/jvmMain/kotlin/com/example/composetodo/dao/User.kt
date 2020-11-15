package com.example.composetodo.dao

import com.example.composetodo.definitions.Todos
import com.example.composetodo.definitions.Users
import io.ktor.auth.*
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

// TODO wait for Exposed DTO support
class User(id: EntityID<UUID>) : UUIDEntity(id), Principal {
    companion object : UUIDEntityClass<User>(Users)

    var firstName by Users.firstName
    var lastName by Users.lastName

    val todos by Todo referrersOn Todos.user
}
