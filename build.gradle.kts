plugins {
    // Apache 2, https://github.com/JetBrains/kotlin/releases/latest
    kotlin("multiplatform") version "1.4.32" apply false
    kotlin("plugin.serialization") version "1.5.0" apply false
    id("com.android.application") version "7.0.0-alpha13" apply false
    id("org.jetbrains.compose") version "0.4.0-build184" apply false
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

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/hfhbd/*")
            credentials {
                username = System.getProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
                password = System.getProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
