plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
}

kotlin {
    js(IR) {
        browser()
    }
    iosArm64()
    iosSimulatorArm64()

    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()

    jvm()

    explicitApi()

    sourceSets {
        commonMain {
            dependencies {
                api(compose.runtime)
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

                api("app.softwork:kotlinx-uuid-core:0.0.17")

                val ktor = "2.1.3"
                api("io.ktor:ktor-client-core:$ktor")
                api("io.ktor:ktor-resources:$ktor")
                api("io.ktor:ktor-client-resources:$ktor")
                api("io.ktor:ktor-client-content-negotiation:$ktor")
                api("io.ktor:ktor-serialization-kotlinx-json:$ktor")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
