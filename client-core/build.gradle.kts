import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm()
    ios {
        binaries {
            framework {
                baseName = "composetodo"
            }
        }
    }
    js(IR) {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":shared"))
                // Apache 2, https://github.com/Kotlin/kotlinx.coroutines/releases/latest
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3-native-mt")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting {
            dependencies {

            }
        }

        val iosMain by getting {
            dependencies {
                // Apache 2, https://github.com/ktorio/ktor/releases/latest
                implementation("io.ktor:ktor-client-ios:1.5.4")
            }
        }
        val iosTest by getting
    }
}

tasks {
    val packForXcode by creating(Sync::class) {
        group = "build"
        val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
        val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
        val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
        val framework =
            kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
        inputs.property("mode", mode)
        dependsOn(framework.linkTask)
        val targetDir = File(buildDir, "xcode-frameworks")
        from({ framework.outputDirectory })
        into(targetDir)
    }
    build { dependsOn(packForXcode) }
}
