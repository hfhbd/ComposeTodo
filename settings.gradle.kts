pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "ComposeTodo"
include(":shared")

include(":backend")

include(":client-core")
//include(":androidApp")
include(":desktop")
