import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
    kotlin("multiplatform")
}

kotlin {
    iosArm64 {
        binaries {
            framework {
                baseName = "composetodo"
                export(projects.shared)
                // Export transitively.
                transitiveExport = true
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.shared)
                // Apache 2, https://github.com/Kotlin/kotlinx.coroutines/releases/latest
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1-native-mt")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val iosArm64Main by getting {
            dependencies {
                // Apache 2, https://github.com/ktorio/ktor/releases/latest
                implementation("io.ktor:ktor-client-ios:1.6.1")
            }
        }
    }
}

tasks {
    val packForXcode by creating(Sync::class) {
        group = "build"
        val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
        val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
        val targetName = "iosArm64"
        val framework =
            kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
        inputs.property("mode", mode)
        dependsOn(framework.linkTask)
        val targetDir = File(buildDir, "xcode-frameworks")
        from({ framework.outputDirectory })
        into(targetDir)
    }
    build {
        dependsOn(packForXcode)
    }
}
