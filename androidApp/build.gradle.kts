plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
}

android {
    namespace = "app.softwork.composetodo"
    compileSdk = 31

    defaultConfig {
        applicationId = "app.softwork.composetodo"
        minSdk = 26
        targetSdk = 31
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
    implementation("androidx.activity:activity-compose:1.5.0")
}

kotlin.target.compilations.all {
    kotlinOptions.jvmTarget = "1.8"
}
