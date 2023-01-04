import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.*

plugins {
    com.android.library
    org.jetbrains.kotlin.multiplatform
    org.jetbrains.compose
    app.cash.sqldelight
    androidLibrary
}

sqldelight {
    database("ComposeTodoDB") {
        packageName = "app.softwork.composetodo"
        deriveSchemaFromMigrations = true
        verifyMigrations = true
    }
}

kotlin {
    android()
    jvm("desktop")

    val xcf = XCFramework()
    fun KotlinNativeTarget.config() {
        binaries {
            framework {
                xcf.add(this)
                export(projects.shared)
                export("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                export("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                export("app.softwork:kotlinx-uuid-core:0.0.17")
                embedBitcode = BitcodeEmbeddingMode.DISABLE
            }
        }
    }
    iosArm64 {
        config()
    }
    iosSimulatorArm64 {
        config()
    }

    js(IR) {
        browser()
    }

    sourceSets {
        val sqlDelight = "2.0.0-alpha04"
        val ktor = "2.2.2"
        commonMain {
            dependencies {
                api(projects.shared)
                implementation("app.cash.sqldelight:coroutines-extensions:$sqlDelight")

                api("io.ktor:ktor-client-logging:$ktor")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
            }
        }

        val androidMain by getting {
            dependencies {
                api("app.cash.sqldelight:android-driver:$sqlDelight")
                api("io.ktor:ktor-client-android:$ktor")
                api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
            }
        }

        val iosArm64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktor")
                implementation("app.cash.sqldelight:native-driver:$sqlDelight")
            }
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosArm64Main)
        }

        val iosArm64Test by getting

        val iosSimulatorArm64Test by getting {
            dependsOn(iosArm64Test)
        }
        val jsMain by getting {
            dependencies {
                api("app.cash.sqldelight:sqljs-driver:$sqlDelight")
            }
        }
    }
}

android {
    namespace = "app.softwork.composetodo.clients"
}

tasks {
    val assembleXCFramework by this
    assemble {
        dependsOn(assembleXCFramework)
    }
}
