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
                api("org.jetbrains.kotlinx.experimental:kotlinx-uuid-core:0.0.1")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jsMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx.experimental:kotlinx-uuid-core-js:0.0.1")
            }
        }
        val iosArm64Main by getting {
            dependencies {
                api("org.jetbrains.kotlinx.experimental:kotlinx-uuid-core-iosarm64:0.0.1")
            }
        }
        val iosX64Main by getting {
            dependencies {
                api("org.jetbrains.kotlinx.experimental:kotlinx-uuid-core-iosx64:0.0.1")
            }
        }
        val jvmMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx.experimental:kotlinx-uuid-core-jvm:0.0.1")
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