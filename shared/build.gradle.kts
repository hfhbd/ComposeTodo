plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    kotlin("plugin.compose")
}

kotlin {
    jvmToolchain(8)

    jvm()

    js {
        browser()
    }

    macosArm64()

    iosArm64()
    iosSimulatorArm64()

    explicitApi()

    sourceSets {
        commonMain {
            dependencies {
                api(libs.compose.runtime)
                api(libs.coroutines.core)
                api(libs.serialization.json)
                api(libs.datetime)

                api(libs.ktor.client.core)
                api(libs.ktor.resources)
                api(libs.ktor.client.resources)
                api(libs.ktor.client.content.negotiation)
                api(libs.ktor.serialization.kotlinx.json)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
