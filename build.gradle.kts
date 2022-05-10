import io.gitlab.arturbosch.detekt.*
import org.jetbrains.compose.*

plugins {
    // Apache 2, https://github.com/JetBrains/kotlin/releases/latest
    kotlin("multiplatform") version "1.6.21" apply false
    kotlin("plugin.serialization") version "1.6.21" apply false
    id("com.android.application") version "7.0.4" apply false
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev682" apply false
    id("com.squareup.sqldelight") version "1.5.3" apply false
    id("org.jetbrains.kotlinx.kover") version "0.5.0"
    id("io.gitlab.arturbosch.detekt") version "1.20.0"
}

allprojects {
    repositories {
        mavenCentral()
        google()

        jetbrainsCompose()
    }
}

detekt {
    source = files(rootProject.rootDir)
    parallel = true
    buildUponDefaultConfig = true
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.20.0")
}

tasks {
    fun SourceTask.config() {
        include("**/*.kt")
        exclude("**/*.kts")
        exclude("**/resources/**")
        exclude("**/generated/**")
        exclude("**/build/**")
    }
    withType<DetektCreateBaselineTask>().configureEach {
        config()
    }
    withType<Detekt>().configureEach {
        config()

        reports {
            sarif.required.set(true)
        }
    }
}
