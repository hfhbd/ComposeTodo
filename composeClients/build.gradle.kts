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

        val desktopMain by getting {
            dependencies {

            }
        }

        val androidMain by getting {
            dependencies {
                implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")
            }
        }
    }
}

android {
    namespace = "app.softwork.composetodo.composeclients"
}
