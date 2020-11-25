package com.example.composetodo

import kotlinx.uuid.UUID
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable

//TODO: remove uuid 0.0.2
abstract class KotlinxUUIDEntity(id: EntityID<UUID>) : Entity<UUID>(id)

abstract class KotlinxUUIDEntityClass<out E: KotlinxUUIDEntity>(table: IdTable<UUID>, entityType: Class<E>? = null) : EntityClass<UUID, E>(table, entityType)
