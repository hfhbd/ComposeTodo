plugins {
    id("androidLibrary")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
}

kotlin {
    jvmToolchain(8)

    applyDefaultHierarchyTemplate()

    androidTarget()
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                api(projects.clients)
                api(compose.foundation)
                api(compose.material)
                api(compose.preview)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        androidMain {
            dependencies {
                implementation(libs.composeMaterialDialogs.datetime)
            }
        }
    }
}

android {
    namespace = "app.softwork.composetodo.composeclients"
}
