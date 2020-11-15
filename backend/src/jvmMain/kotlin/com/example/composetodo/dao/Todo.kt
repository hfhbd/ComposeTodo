package com.example.composetodo.dao

import com.example.composetodo.definitions.Todos
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID

// TODO wait for Exposed DTO support
class Todo(id: EntityID<java.util.UUID>): UUIDEntity(id) {
    companion object: UUIDEntityClass<Todo>(Todos)

    var user by User referencedOn Todos.user
    var title by Todos.title
    var until by Todos.until
    var finished by Todos.finished
}
