package app.softwork.composetodo

import platform.Foundation.NSUUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Converts this [NSUUID] value to a [Uuid] value
 * by using the [UUIDString](platform.Foundation.NSUUID.UUIDString) representation.
 */
@ExperimentalUuidApi
public fun NSUUID.toKotlinUuid(): Uuid = Uuid.parse(UUIDString)

/**
 * Converts this [Uuid] value to a [NSUUID] value
 * by using the default [kotlin.uuid.Uuid.toString] representation.
 */
@ExperimentalUuidApi
public fun Uuid.toNsUUID(): NSUUID = NSUUID(toString())
