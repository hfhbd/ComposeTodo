plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
    id("app.cash.licensee")
}

android {
    namespace = "app.softwork.composetodo"
    compileSdk = 32

    defaultConfig {
        applicationId = "app.softwork.composetodo"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        // Sets Java compatibility to Java 8
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        release {
            isMinifyEnabled = true
        }
    }
}

dependencies {
    implementation(projects.composeClients)
    implementation("androidx.activity:activity-compose:1.5.1")
}

licensee {
    allow("Apache-2.0")
    allow("MIT")
}
