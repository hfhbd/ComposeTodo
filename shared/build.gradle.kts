plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    js(IR) {
        browser()
    }
    ios()
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                useIR = true
            }
        }
        withJava()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(json())
                implementation(dateTime())
                implementation("org.jetbrains.kotlinx.experimental:kotlinx-uuid-core:0.0.1-SNAPSHOT")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}

/**
 * [DateTime](https://github.com/Kotlin/kotlinx-datetime/releases)
 */
fun dateTime() = "org.jetbrains.kotlinx:kotlinx-datetime:0.1.0"

/**
 * [Serialization](https://github.com/Kotlin/kotlinx.serialization/releases/latest)
 */
fun json() = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1"