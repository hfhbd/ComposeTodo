package app.softwork.composetodo.dao

import app.softwork.cloudkitclient.*
import app.softwork.cloudkitclient.values.*
import io.ktor.server.auth.*
import kotlinx.serialization.*

@Serializable
data class User(
    override val recordName: String,
    override val fields: Fields,

    override var created: TimeInformation? = null,
    override var modified: TimeInformation? = null,
    override var deleted: Boolean? = false,

    override val pluginFields: PluginFields = PluginFields(),
    override var recordChangeTag: String? = null,
    override val zoneID: ZoneID = ZoneID.default
) : Record<User.Fields> {

    override val recordType: String = Companion.recordType

    companion object : Record.Information<Fields, User> {
        override val recordType: String = "User"

        override fun fields() = listOf(Fields::firstName, Fields::lastName, Fields::password)
        override fun fieldsSerializer() = Fields.serializer()
    }

    @Serializable
    data class Fields(
        val password: Value.String?,
        val firstName: Value.String,
        val lastName: Value.String
    ) : Record.Fields
}
