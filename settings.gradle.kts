pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

plugins {
    id("MyRepos")
}

rootProject.name = "ComposeTodo"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
// enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

include(":shared")

include(":backend")

include(":clients")
include(":composeClients")
include(":web")
include(":androidApp")
include(":desktop")
