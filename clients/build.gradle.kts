import org.jetbrains.kotlin.gradle.plugin.mpp.apple.*

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()

    val xcf = XCFramework()
    iosArm64 {
        binaries {
            framework {
                baseName = "shared"
                export(projects.shared)
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
                api("io.ktor:ktor-client-ios:1.6.3")
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
