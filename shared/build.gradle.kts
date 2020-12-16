plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    js {
        browser()
    }
    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
        withJava()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(json())
                api(dateTime())
                api(uuid())
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

/**
 * [UUID](https://github.com/cy6erGn0m/kotlinx-uuid/releases)
 */
fun uuid() = "org.jetbrains.kotlinx.experimental:kotlinx-uuid-core:0.0.2"
