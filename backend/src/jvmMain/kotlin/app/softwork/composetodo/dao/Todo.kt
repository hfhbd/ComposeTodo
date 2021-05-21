package app.softwork.composetodo.dao

import app.softwork.cloudkitclient.*
import app.softwork.cloudkitclient.values.*
import kotlinx.serialization.*

@Serializable
data class Todo(
    override val recordName: String,
    override val fields: Fields,

    override var created: TimeInformation? = null,
    override var modified: TimeInformation? = null,
    override var deleted: Boolean? = null,

    override val pluginFields: PluginFields = PluginFields(),
    override var recordChangeTag: String? = null,

    override val zoneID: ZoneID = ZoneID.default
) : Record<Todo.Fields> {
    override val recordType: String = Companion.recordType

    companion object : Record.Information<Fields, Todo> {
        override val recordType: String = "Todos"

        override fun fields() = listOf(Fields::title, Fields::user, Fields::until, Fields::finished)

        override fun fieldsSerializer() = Fields.serializer()
    }

    @Serializable
    data class Fields(
        val user: Value.Reference<User.Fields, User>,
        val title: Value.String,
        val until: Value.DateTime? = null,
        val finished: Value.String
    ) : Record.Fields
}
