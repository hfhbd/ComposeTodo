plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("org.jetbrains.compose")
}

android {
    compileSdk = 30

    defaultConfig {
        applicationId = "app.softwork.composetodo"
        minSdk = 21
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
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
    implementation(projects.clients)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    implementation("io.ktor:ktor-client-android:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1-native-mt")

    implementation("androidx.activity:activity-compose:1.3.1")

    val roomVersion = "2.3.0"

    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    testImplementation("androidx.room:room-testing:$roomVersion")

    testImplementation(kotlin("test-junit"))
    androidTestImplementation(kotlin("test-junit"))
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
}
