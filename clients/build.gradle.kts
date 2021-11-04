import org.jetbrains.kotlin.gradle.plugin.mpp.apple.*

plugins {
    kotlin("multiplatform")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("ComposeTodoDB") {
        packageName = "app.softwork.composetodo"
    }
}

kotlin {
    jvm()

    val xcf = XCFramework()
    iosArm64 {
        binaries {
            framework {
                baseName = "shared"
                xcf.add(this)
                export(projects.shared)
                export("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt")
                transitiveExport = true
            }
        }
    }
    iosSimulatorArm64 {
        binaries {
            framework {
                baseName = "shared"
                xcf.add(this)
                export(projects.shared)
                export("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt")
                transitiveExport = true
            }
        }
    }

    js(IR) {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.shared)
                implementation("com.squareup.sqldelight:coroutines-extensions:1.6.0-SNAPSHOT")
                implementation("app.softwork:kotlinx-uuid-sqldelight:0.0.12")
                implementation("io.ktor:ktor-client-logging:1.6.5")
            }
        }
        commonTest {
            dependencies {
                api(kotlin("test"))
            }
        }

        val iosArm64Main by getting {
            dependencies {
                // Apache 2, https://github.com/ktorio/ktor/releases/latest
                implementation("io.ktor:ktor-client-ios:1.6.5")
                implementation("com.squareup.sqldelight:native-driver:1.5.2")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core") {
                    version {
                        strictly("1.5.2-native-mt")
                    }
                }
            }
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosArm64Main)
        }
    }
}

tasks {
    val assembleClientsXCFramework by this
    build {
        dependsOn(assembleClientsXCFramework)
    }
}
