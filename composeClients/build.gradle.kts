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
    kotlinCompilerPlugin.set(dependencies.compiler.forKotlin("1.8.0"))
    kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=1.8.10")
}
