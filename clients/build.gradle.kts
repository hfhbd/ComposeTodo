import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.*

plugins {
    id("androidLibrary")
    kotlin("multiplatform")
    kotlin("plugin.compose")
    id("app.cash.sqldelight")
}

sqldelight {
    databases.register("ComposeTodoDB") {
        packageName.set("app.softwork.composetodo")
        deriveSchemaFromMigrations.set(true)
        generateAsync.set(true)
    }
}

kotlin {
    jvmToolchain(8)

    applyDefaultHierarchyTemplate()

    androidTarget()
    jvm()

    js {
        browser()
    }

    val xcf = XCFramework()
    fun KotlinNativeTarget.config() {
        binaries {
            framework {
                xcf.add(this)
                export(projects.shared)
                export(libs.coroutines.core)
                export(libs.datetime)
            }
        }
    }

    macosArm64 { config() }

    iosArm64 { config() }
    iosSimulatorArm64 { config() }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.shared)
                implementation(libs.sqldelight.coroutinesExtensions)

                api(libs.ktor.client.logging)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.coroutines.test)
            }
        }

        androidMain {
            dependencies {
                api(libs.sqldelight.androidDriver)
                api(libs.ktor.client.android)
                api(libs.androidx.viewmodel.lifecycle)
            }
        }

        appleMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
                implementation(libs.sqldelight.nativeDriver)
            }
        }
    }
}

android {
    namespace = "app.softwork.composetodo.clients"
}
