plugins {
    // Apache 2, https://github.com/JetBrains/kotlin/releases/latest
    kotlin("multiplatform") version "1.4.31" apply false
    kotlin("plugin.serialization") version "1.4.32" apply false
    id("com.android.application") version "7.0.0-alpha12" apply false
    id("org.jetbrains.compose") version "0.4.0-build178" apply false
}

repositories {
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

allprojects {
    repositories {
        mavenCentral()
        google()
        maven(url = "https://dl.bintray.com/cy6ergn0m/uuid")
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven(url = "https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers")

        maven(url = "https://kotlin.bintray.com/kotlinx/") // https://github.com/Kotlin/kotlinx-datetime/issues/40
        jcenter() // https://github.com/JetBrains/Exposed/issues/1160
    }
}
