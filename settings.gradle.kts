pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }
}

rootProject.name = "ComposeTodo"
include(":shared")

include(":backend")

include(":client-core")
//include(":androidApp")
include(":webApp")
