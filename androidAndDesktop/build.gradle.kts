plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "0.4.0-rc2"
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                api(projects.shared)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

       val jvmMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
            }
        }
        val jvmTest by getting {
            dependencies {

            }
        }
    }
}
