import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    androidApplication
    org.jetbrains.compose
    license
}

android {
    namespace = "app.softwork.composetodo"

    defaultConfig {
        applicationId = "app.softwork.composetodo"
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
}

dependencies {
    implementation(projects.composeClients)
    implementation("androidx.activity:activity-compose:1.7.0")
}

licensee {
    allow("MIT")
}
