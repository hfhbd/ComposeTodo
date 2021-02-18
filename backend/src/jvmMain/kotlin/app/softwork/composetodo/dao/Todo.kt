package app.softwork.composetodo.dao

import app.softwork.composetodo.definitions.Todos
import kotlinx.uuid.UUID
import kotlinx.uuid.exposed.KotlinxUUIDEntity
import kotlinx.uuid.exposed.KotlinxUUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID

// TODO wait for Exposed DTO support
class Todo(id: EntityID<UUID>): KotlinxUUIDEntity(id) {
    companion object: KotlinxUUIDEntityClass<Todo>(Todos)

    var user by User referencedOn Todos.user
    var title by Todos.title
    var until by Todos.until
    var finished by Todos.finished
}
