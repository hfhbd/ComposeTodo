import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                useIR = true
            }
        }
        withJava()
    }
    ios {
        binaries {
            framework {
                baseName = "clientcore"
            }
        }
    }
    js {
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
                implementation(ktor("client-core"))
                implementation(coroutines("core"))
            }
        }
        commonTest

        val jvmMain by getting {
            dependencies {
                api(ktor("client-android"))
                api(coroutines("android"))
            }
        }
        val jvmTest by getting

        val iosMain by getting {
            dependencies {
                implementation(ktor("client-ios"))
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

/**
 * [Coroutines](https://github.com/Kotlin/kotlinx.coroutines/releases/latest)
 */
fun coroutines(module: String) = "org.jetbrains.kotlinx:kotlinx-coroutines-$module:1.4.2-native-mt"

/**
 * [Ktor](https://github.com/ktorio/ktor/releases/latest)
 */
fun ktor(module: String) = "io.ktor:ktor-$module:1.4.3"

fun <T> NamedDomainObjectContainer<T>.release(action: T.() -> Unit) = getByName("release", action)
