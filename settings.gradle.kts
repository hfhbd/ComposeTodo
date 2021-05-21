pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
    }
}

rootProject.name = "ComposeTodo"
include(":shared")

include(":backend")

include(":client-core")
include(":androidApp")
include(":desktop")
//include(":web")
