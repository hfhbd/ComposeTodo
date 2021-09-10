import org.jetbrains.kotlin.gradle.plugin.mpp.apple.*

plugins {
    kotlin("multiplatform")
    id("com.squareup.sqldelight") version "1.5.1"
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
                export(projects.shared)
                export("com.squareup.sqldelight:coroutines-extensions:1.5.1")
                export("app.softwork:kotlinx-uuid-sqldelight:0.0.11")
                // Export transitively.
                transitiveExport = true
                xcf.add(this)
            }
        }
    }

    js(IR) {
        browser {
            binaries.library()
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.shared)
                api("com.squareup.sqldelight:coroutines-extensions:1.5.1")
                api("app.softwork:kotlinx-uuid-sqldelight:0.0.11")
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
                api("io.ktor:ktor-client-ios:1.6.4")
                implementation("com.squareup.sqldelight:native-driver:1.5.1")
            }
        }
    }
}

tasks {
    val assembleClientsXCFramework by this
    build {
        dependsOn(assembleClientsXCFramework)
    }
}
