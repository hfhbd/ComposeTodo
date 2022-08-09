import io.gitlab.arturbosch.detekt.*

plugins {
    kotlin("multiplatform") version "1.7.10" apply false
    kotlin("plugin.serialization") version "1.7.10" apply false
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev755" apply false
    id("app.cash.sqldelight") version "2.0.0-alpha03" apply false

    id("com.android.application") version "7.2.2" apply false
    id("com.google.cloud.tools.jib") version "3.2.1" apply false

    id("app.cash.licensee") version "1.5.0" apply false
    id("org.jetbrains.kotlinx.kover") version "0.5.1"
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
}

buildscript {
    dependencies {
        classpath("org.apache.commons:commons-compress:1.21")
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()

        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

detekt {
    source = files(rootProject.rootDir)
    parallel = true
    autoCorrect = true
    buildUponDefaultConfig = true
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
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
