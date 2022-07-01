plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        commonMain {
            dependencies {
                api(projects.clients)
                api(compose.runtime)
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
                implementation("io.github.vanpra.compose-material-dialogs:datetime:0.7.2")
            }
        }
    }
}

android {
    namespace = "app.softwork.composetodo.composeclients"
    compileSdk = 32

    defaultConfig {
        minSdk = 26
        targetSdk = 32
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
