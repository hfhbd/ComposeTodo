plugins {
    val kotlinVersion = "1.4.21" // Apache 2, https://github.com/JetBrains/kotlin/releases/latest
    kotlin("multiplatform") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
    id("com.android.application") version "7.0.0-alpha05" apply false
}

repositories {
    jcenter()
    google()
}

allprojects {
    repositories {
        jcenter()
        google()
        maven("https://dl.bintray.com/cy6ergn0m/uuid")
        maven(url = "https://kotlin.bintray.com/kotlinx/") // kotlinx.datetime
    }
}
