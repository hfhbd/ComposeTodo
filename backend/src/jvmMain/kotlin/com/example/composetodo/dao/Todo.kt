package com.example.composetodo.dao

import com.example.composetodo.KotlinxUUIDEntity
import com.example.composetodo.KotlinxUUIDEntityClass
import com.example.composetodo.definitions.Todos
import kotlinx.uuid.UUID
import org.jetbrains.exposed.dao.id.EntityID

// TODO wait for Exposed DTO support
class Todo(id: EntityID<UUID>): KotlinxUUIDEntity(id) {
    companion object: KotlinxUUIDEntityClass<Todo>(Todos)

    var user by User referencedOn Todos.user
    var title by Todos.title
    var until by Todos.until
    var finished by Todos.finished
}
