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
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
    id("com.gradle.enterprise") version "3.15"
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

include(":shared")

include(":backend")

include(":clients")
include(":composeClients")
include(":web")
include(":androidApp")
include(":desktop")
