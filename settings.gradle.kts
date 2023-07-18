pluginManagement {
    includeBuild("build-logic")
    repositories {
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

plugins {
    id("MyRepos")
    id("com.gradle.enterprise") version "3.14"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        if (System.getenv("CI") != null) {
            publishAlways()
            tag("CI")
        }
    }
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
