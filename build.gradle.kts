plugins {
    // Apache 2, https://github.com/JetBrains/kotlin/releases/latest
    kotlin("multiplatform") version "1.5.21" apply false
    kotlin("plugin.serialization") version "1.5.21" apply false
    id("com.android.application") version "7.0.1" apply false
    id("org.jetbrains.compose") version "0.5.0-build262" apply false
}

repositories {
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

allprojects {
    repositories {
        mavenCentral()
        google()

        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
