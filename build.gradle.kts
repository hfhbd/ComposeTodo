plugins {
    // Apache 2, https://github.com/JetBrains/kotlin/releases/latest
    kotlin("multiplatform") version "1.6.10" apply false
    kotlin("plugin.serialization") version "1.6.10" apply false
    id("com.android.application") version "7.0.4" apply false
    id("org.jetbrains.compose") version "1.0.1" apply false
    id("com.squareup.sqldelight") version "1.5.3" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()

        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin> {
    val nodeM1Version = "16.13.1"
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>().nodeVersion = nodeM1Version
}
