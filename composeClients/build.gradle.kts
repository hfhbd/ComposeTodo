import org.jetbrains.kotlin.gradle.plugin.mpp.apple.*

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm()

    /*js(IR) {
        browser {
            binaries.library()
        }
    }*/

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

        val jvmMain by getting {
            dependencies {

            }
        }
        val jvmTest by getting {
            dependencies {

            }
        }
    }
}
