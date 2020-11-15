package com.example.composetodo.definitions

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object Todos: UUIDTable() {
    val user = reference("userID", Users)
    val title = varchar("title", 512)
    val until = datetime("until")
    val finished = bool("finished")
}