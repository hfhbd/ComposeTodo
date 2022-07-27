plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    js(IR) {
        browser()
    }
    iosArm64 {
        compilations["main"].kotlinOptions.freeCompilerArgs += "-Xexport-kdoc"
    }
    iosSimulatorArm64 {
        compilations["main"].kotlinOptions.freeCompilerArgs += "-Xexport-kdoc"
    }
    jvm()

    explicitApi()

    sourceSets {
        commonMain {
            dependencies {
                // Apache 2, https://github.com/Kotlin/kotlinx.serialization/releases/latest
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

                // Apache 2, https://github.com/Kotlin/kotlinx-datetime/releases
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

                // Apache 2, https://github.com/hfhbd/kotlinx-uuid/releases
                api("app.softwork:kotlinx-uuid-core:0.0.16-sqldelight2a03")

                // Apache 2, https://github.com/ktorio/ktor/releases/latest
                val ktor = "2.0.3"
                api("io.ktor:ktor-client-core:$ktor")
                api("io.ktor:ktor-resources:$ktor")
                api("io.ktor:ktor-client-resources:$ktor")
                api("io.ktor:ktor-client-content-negotiation:$ktor")
                api("io.ktor:ktor-serialization-kotlinx-json:$ktor")

                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            }
        }
        commonTest {
            dependencies {
                api(kotlin("test"))
            }
        }
    }
}
