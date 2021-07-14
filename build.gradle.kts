plugins {
    // Apache 2, https://github.com/JetBrains/kotlin/releases/latest
    kotlin("multiplatform") version "1.5.10" apply false
    kotlin("plugin.serialization") version "1.5.10" apply false
    id("com.android.application") version "4.2.2" apply false
    id("org.jetbrains.compose") version "0.5.0-build245" apply false
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

        maven(url = "https://maven.pkg.github.com/hfhbd/*") {
            credentials {
                username = System.getProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
                password = System.getProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
