plugins {
    val kotlinVersion = "1.4.20-RC"
    kotlin("multiplatform") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
    id("com.android.library") version "4.2.0-alpha16" apply false
}

repositories {
    jcenter()
    google()
}

allprojects {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/cy6erGn0m/kotlinx-uuid")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        jcenter()
        google()
        maven(url = "https://kotlin.bintray.com/kotlinx/") // kotlinx.datetime
    }
}