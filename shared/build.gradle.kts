plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    js(IR) {
        browser()
    }
    ios()
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                // Apache 2, https://github.com/Kotlin/kotlinx.serialization/releases/latest
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")

                // Apache 2, https://github.com/Kotlin/kotlinx-datetime/releases
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.2.0")

                // Apache 2, https://github.com/hfhbd/kotlinx-uuid/releases
                api("app.softwork:kotlinx-uuid-core:0.0.5")

                // Apache 2, https://github.com/ktorio/ktor/releases/latest
                api("io.ktor:ktor-client-core:1.5.4")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
