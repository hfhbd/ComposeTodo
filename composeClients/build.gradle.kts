plugins {
    id("androidLibrary")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvmToolchain(8)

    androidTarget()
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
                implementation(libs.composeMaterialDialogs.datetime)
            }
        }
    }
}

android {
    namespace = "app.softwork.composetodo.composeclients"
}

compose {
    kotlinCompilerPlugin.set("1.5.7")
}
