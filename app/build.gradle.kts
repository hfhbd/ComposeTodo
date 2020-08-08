plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    kotlin("plugin.serialization")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.example.jetpacknews"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = listOf("-Xallow-jvm-ir-dependencies", "-Xskip-prerelease-check", "-Xinline-classes", "-Xopt-in=kotlin.RequiresOptIn")
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

dependencies {
    implementation("androidx.core","core-ktx", "1.3.1")
    implementation("androidx.appcompat", "appcompat", "1.2.0")

    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-runtime", "1.0-M1-1.4.0-rc")

    val composeVersion: String by project
    implementation("androidx.compose.runtime", "runtime", composeVersion)
    implementation("androidx.compose.ui", "ui", composeVersion)
    implementation("androidx.compose.foundation", "foundation-layout", composeVersion)
    implementation("androidx.compose.material", "material", composeVersion)
    implementation("androidx.compose.material", "material-icons-extended", composeVersion)
    implementation("androidx.compose.foundation", "foundation", composeVersion)
    implementation("androidx.compose.animation", "animation", composeVersion)
    implementation("androidx.ui", "ui-tooling", composeVersion)
    implementation("org.jetbrains.kotlinx","kotlinx-coroutines-android", "1.3.8-1.4.0-rc")
    implementation("androidx.activity", "activity-ktx", "1.1.0")

    testImplementation(kotlin("test-junit"))
    androidTestImplementation("androidx.test", "rules", "1.2.0")
    androidTestImplementation("androidx.test", "runner","1.2.0")
    androidTestImplementation("androidx.ui", "ui-test", composeVersion)
}

fun <T> NamedDomainObjectContainer<T>.release(action: T.() -> Unit): T =
    getByName("release", action)
