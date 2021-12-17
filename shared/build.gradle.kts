plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    js(IR) {
        browser()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                // Apache 2, https://github.com/Kotlin/kotlinx.serialization/releases/latest
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")

                // Apache 2, https://github.com/Kotlin/kotlinx-datetime/releases
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")

                // Apache 2, https://github.com/hfhbd/kotlinx-uuid/releases
                api("app.softwork:kotlinx-uuid-core:0.0.12")

                // Apache 2, https://github.com/ktorio/ktor/releases/latest
                api("io.ktor:ktor-client-core:1.6.7")

                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-RC3")
            }
        }
        commonTest {
            dependencies {
                api(kotlin("test"))
            }
        }
    }
}
