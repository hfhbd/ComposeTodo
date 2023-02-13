import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    androidLibrary
    org.jetbrains.kotlin.multiplatform
    org.jetbrains.compose
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        commonMain {
            dependencies {
                api(projects.clients)
                api(compose.foundation)
                api(compose.material)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        named("androidMain") {
            dependencies {
                implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")
            }
        }
    }
}

android {
    namespace = "app.softwork.composetodo.composeclients"
}

compose {
    kotlinCompilerPlugin.set("1.4.0")
}

tasks.withType(KotlinCompile::class).configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-P", "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=1.8.10"
        )
    }
}
