import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                useIR = true
            }
        }
    }
    ios {
        binaries {
            framework {
                baseName = "composetodo"
            }
        }
    }
    js(IR) {
        browser {
            binaries.executable()
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

                // Apache 2, https://github.com/ktorio/ktor/releases/latest
                api("io.ktor:ktor-client-core:1.5.3")
                // Apache 2, https://github.com/Kotlin/kotlinx.coroutines/releases/latest
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3-native-mt")
            }
        }
        commonTest {
            dependencies {

            }
        }

        val jsMain by getting {
            dependencies {
                // Apache 2, https://bintray.com/kotlin/kotlin-js-wrappers/kotlin-react
                implementation("org.jetbrains:kotlin-react:17.0.2-pre.154-kotlin-1.5.0")
                implementation("org.jetbrains:kotlin-react-dom:17.0.2-pre.154-kotlin-1.5.0")
                implementation("org.jetbrains:kotlin-react-router-dom:5.2.0-pre.154-kotlin-1.5.0")
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val jvmMain by getting {
            dependencies {
            }
        }
        val jvmTest by getting {
            dependencies {
                api(kotlin("test-junit"))
            }
        }

        val iosMain by getting {
            dependencies {
                // Apache 2, https://github.com/ktorio/ktor/releases/latest
                implementation("io.ktor:ktor-client-ios:1.5.3")
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
