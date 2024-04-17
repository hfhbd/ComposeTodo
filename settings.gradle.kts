pluginManagement {
    includeBuild("gradle/build-logic")
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

plugins {
    id("MyRepos")
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    id("com.gradle.develocity") version "3.17.2"
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/terms-of-service")
        termsOfUseAgree.set("yes")
        publishing {
            val isCI = providers.environmentVariable("CI").isPresent
            onlyIf { isCI }
        }
        tag("CI")
    }
}

rootProject.name = "ComposeTodo"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

include(":shared")

include(":backend")

include(":clients")
include(":composeClients")
include(":web")
include(":androidApp")
include(":desktop")
