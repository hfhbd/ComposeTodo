plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
    //kotlin("plugin.serialization") version "1.4.0"
}

android {
    compileSdkVersion(30)

    defaultConfig {
        applicationId = "com.example.composetodo"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = listOf("-Xopt-in=kotlin.time.ExperimentalTime")
    }

    composeOptions {
        val kotlinVersion: String by project
        kotlinCompilerVersion = kotlinVersion
        val composeVersion: String by project
        kotlinCompilerExtensionVersion = composeVersion
    }

    buildTypes {
        release {
            isMinifyEnabled = true
        }
    }
}

repositories {
    google()
    jcenter()
    maven("https://kotlin.bintray.com/kotlinx")
}

dependencies {
    implementation("androidx.core","core-ktx", "1.3.1")
    implementation("androidx.appcompat", "appcompat", "1.2.0")
    implementation("androidx.activity", "activity-ktx", "1.1.0")

    implementation("org.jetbrains.kotlinx","kotlinx-coroutines-android", "1.3.9")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-core", "1.0.0-RC")

    val composeVersion: String by project
    implementation("androidx.compose.runtime", "runtime", composeVersion)
    implementation("androidx.compose.ui", "ui", composeVersion)
    implementation("androidx.compose.foundation", "foundation-layout", composeVersion)
    implementation("androidx.compose.material", "material", composeVersion)
    implementation("androidx.compose.material", "material-icons-extended", composeVersion)
    implementation("androidx.compose.foundation", "foundation", composeVersion)
    implementation("androidx.compose.animation", "animation", composeVersion)
    implementation("androidx.ui", "ui-tooling", composeVersion)

    testImplementation(kotlin("test-junit5"))
    androidTestImplementation(kotlin("test-junit"))
    androidTestImplementation("androidx.test", "rules", "1.2.0")
    androidTestImplementation("androidx.test", "runner","1.2.0")
    androidTestImplementation("androidx.ui", "ui-test", composeVersion)
}

fun <T> NamedDomainObjectContainer<T>.release(action: T.() -> Unit): T =
    getByName("release", action)
