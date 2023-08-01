import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.compose")
}

kotlin {
    jvmToolchain(8)

    js(IR) {
        browser()
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

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
