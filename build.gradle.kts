plugins {
    // Apache 2, https://github.com/JetBrains/kotlin/releases/latest
    kotlin("multiplatform") version "1.5.31" apply false
    kotlin("plugin.serialization") version "1.6.0" apply false
    id("com.android.application") version "7.0.4" apply false
    id("org.jetbrains.compose") version "1.0.0" apply false
    id("com.squareup.sqldelight") version "1.5.3" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()

        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

// https://youtrack.jetbrains.com/issue/KT-49124
rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension>().apply {
        resolution("@webpack-cli/serve", "1.5.2")
    }
}
