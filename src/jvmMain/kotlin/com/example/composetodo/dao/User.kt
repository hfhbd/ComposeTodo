package com.example.composetodo.dao

import com.example.composetodo.KotlinxUUIDEntity
import com.example.composetodo.KotlinxUUIDEntityClass
import com.example.composetodo.definitions.Todos
import com.example.composetodo.definitions.Users
import io.ktor.auth.*
import kotlinx.uuid.UUID
import org.jetbrains.exposed.dao.id.EntityID

// TODO wait for Exposed DTO support
class User(id: EntityID<UUID>) : KotlinxUUIDEntity(id), Principal {
    companion object : KotlinxUUIDEntityClass<User>(Users)

    var firstName by Users.firstName
    var lastName by Users.lastName

    val todos by Todo referrersOn Todos.user
}
