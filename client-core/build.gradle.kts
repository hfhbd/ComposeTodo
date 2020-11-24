import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

kotlin {
    android()
    ios {
        binaries {
            framework {
                baseName = "shared"
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
                implementation(project(":shared"))
                implementation(ktor("client-core"))
                implementation(coroutines("core"))
            }
        }
        commonTest

        val androidMain by getting {
            dependencies {
                implementation(ktor("client-android"))
                implementation(coroutines("android"))
            }
        }
        val androidTest by getting
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

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

/**
 * [Coroutines](https://github.com/Kotlin/kotlinx.coroutines/releases/latest)
 */
fun coroutines(module: String) = "org.jetbrains.kotlinx:kotlinx-coroutines-$module:1.4.1"

/**
 * [DateTime](https://github.com/Kotlin/kotlinx-datetime/releases)
 */
fun dateTime() = "org.jetbrains.kotlinx:kotlinx-datetime:0.1.0"

/**
 * [Ktor](https://github.com/ktorio/ktor/releases/latest)
 */
fun ktor(module: String) = "io.ktor:ktor-$module:1.4.2"

/**
 * [Serialization](https://github.com/Kotlin/kotlinx.serialization/releases/latest)
 */
fun json() = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1"

fun <T> NamedDomainObjectContainer<T>.release(action: T.() -> Unit) = getByName("release", action)
