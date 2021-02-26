plugins {
    // Apache 2, https://github.com/JetBrains/kotlin/releases/latest
    kotlin("multiplatform") version "1.4.30" apply false
    kotlin("plugin.serialization") version "1.4.31" apply false
    id("com.android.application") version "7.0.0-alpha08" apply false
    id("org.jetbrains.compose") version "0.4.0-build168" apply false
}

repositories {
    jcenter()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

allprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://dl.bintray.com/cy6ergn0m/uuid")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven(url = "https://kotlin.bintray.com/kotlinx/")
        jcenter()
    }
}
