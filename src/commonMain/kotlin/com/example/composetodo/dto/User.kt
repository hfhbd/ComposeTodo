package com.example.composetodo.dto

import kotlinx.serialization.Serializable
import kotlinx.uuid.UUID

@Serializable
data class User(val id: UUID, val firstName: String, val lastName: String)