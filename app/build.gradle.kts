plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
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

    compileOptions {
        coreLibraryDesugaringEnabled = true
        // Sets Java compatibility to Java 8
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        useIR = true
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
    implementation(project(":networking"))
    coreLibraryDesugaring("com.android.tools", "desugar_jdk_libs", "1.0.9")
    implementation("androidx.core", "core-ktx", "1.3.1")
    implementation("androidx.appcompat", "appcompat", "1.2.0")
    implementation("androidx.activity", "activity-ktx", "1.1.0")

    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-android", "1.3.9")
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

    val roomVersion = "2.2.5"

    implementation("androidx.room", "room-runtime", roomVersion)
    kapt("androidx.room", "room-compiler", roomVersion)
    implementation("androidx.room", "room-ktx", roomVersion)
    implementation("androidx.lifecycle", "lifecycle-livedata-ktx", "2.2.0")
    testImplementation("androidx.room", "room-testing", roomVersion)

    testImplementation(kotlin("test-junit"))
    androidTestImplementation(kotlin("test-junit"))
    androidTestImplementation("androidx.test", "rules", "1.2.0")
    androidTestImplementation("androidx.test", "runner", "1.2.0")
    androidTestImplementation("androidx.ui", "ui-test", composeVersion)
}

fun <T> NamedDomainObjectContainer<T>.release(action: T.() -> Unit): T =
    getByName("release", action)
