package com.example.composetodo.definitions

import org.jetbrains.exposed.dao.id.UUIDTable

object Users: UUIDTable() {
    val firstName = varchar("firstName", 128)
    val lastName = varchar("lastName", 128)
}