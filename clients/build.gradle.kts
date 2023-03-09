import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.*

plugins {
    androidLibrary
    org.jetbrains.kotlin.multiplatform
    org.jetbrains.compose
    app.cash.sqldelight
}

sqldelight {
    databases.register("ComposeTodoDB") {
        packageName.set("app.softwork.composetodo")
        deriveSchemaFromMigrations.set(true)
        verifyMigrations.set(true)
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
                embedBitcodeMode.set(BitcodeEmbeddingMode.DISABLE)
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
        val sqlDelight = "2.0.0-alpha05"
        val ktor = "2.2.4"
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

        named("androidMain") {
            dependencies {
                api("app.cash.sqldelight:android-driver:$sqlDelight")
                api("io.ktor:ktor-client-android:$ktor")
                api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
            }
        }

        val iosMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktor")
                implementation("app.cash.sqldelight:native-driver:$sqlDelight")
            }
        }
        val iosTest by creating {
            dependsOn(commonTest.get())
        }
        
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm64Test by getting {
            dependsOn(iosTest)
        }
        
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
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
    val assembleXCFramework by existing
    assemble {
        dependsOn(assembleXCFramework)
    }
}
