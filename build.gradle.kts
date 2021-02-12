plugins {
    // Apache 2, https://github.com/JetBrains/kotlin/releases/latest
    kotlin("multiplatform") version "1.4.21-2" apply false
    kotlin("plugin.serialization") version "1.4.30" apply false
    id("com.android.application") version "7.0.0-alpha06" apply false
}

repositories {
    jcenter()
    google()
}

allprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://dl.bintray.com/cy6ergn0m/uuid")
        maven(url = "https://kotlin.bintray.com/kotlinx/")
        jcenter()
    }
}
