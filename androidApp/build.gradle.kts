plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp") version "1.5.30-1.0.0"
    id("org.jetbrains.compose")
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "app.softwork.composetodo"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true // java.time needs minSDK 26
        // Sets Java compatibility to Java 8
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }
}

dependencies {
    implementation(projects.composeClients)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    implementation("io.ktor:ktor-client-android:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2-native-mt")

    implementation("androidx.activity:activity-compose:1.3.1")

    val room = "2.4.0-alpha04"
    implementation("androidx.room:room-runtime:$room")
    ksp("androidx.room:room-compiler:$room")
    implementation("androidx.room:room-ktx:$room")
    testImplementation("androidx.room:room-testing:$room")

    testImplementation(kotlin("test-junit"))
    androidTestImplementation(kotlin("test-junit"))
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
}
