plugins {
    id("androidApplication")
    id("org.jetbrains.compose")
    id("license")
}

kotlin.jvmToolchain(8)

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

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(projects.composeClients)
    implementation(libs.androidx.activity.compose)
}

licensee {
    allow("MIT")
}
