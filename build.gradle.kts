plugins {
    // Apache 2, https://github.com/JetBrains/kotlin/releases/latest
    kotlin("multiplatform") version "1.5.30" apply false
    kotlin("plugin.serialization") version "1.5.30" apply false
    id("com.android.application") version "4.2.2" apply false
    id("org.jetbrains.compose") version "1.0.0-alpha4-build331" apply false
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
