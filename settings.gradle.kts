include(":app")
rootProject.name = "My ApplicationResult"

pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("plugin.serialization") version kotlinVersion
    }
}
