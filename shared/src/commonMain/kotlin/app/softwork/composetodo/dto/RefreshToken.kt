package app.softwork.composetodo.dto

import kotlinx.serialization.*
import kotlin.jvm.*

@Serializable
@JvmInline
public value class RefreshToken(public val value: String)
