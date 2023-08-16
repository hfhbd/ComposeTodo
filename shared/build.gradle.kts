import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
}

kotlin {
    jvmToolchain(8)

    jvm()

    js(IR) {
        browser()
    }

    macosX64()
    macosArm64()

    iosArm64()
    iosSimulatorArm64()

    watchosArm32()
    watchosArm64()
    // watchosDeviceArm64()
    watchosSimulatorArm64()

    tvosArm64()
    tvosSimulatorArm64()

    explicitApi()

    sourceSets {
        commonMain {
            dependencies {
                api(compose.runtime)
                api(libs.coroutines.core)
                api(libs.serialization.json)
                api(libs.datetime)

                api(libs.uuid.core)

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

tasks.withType<KotlinJsCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xklib-enable-signature-clash-checks=false",
    )
}
