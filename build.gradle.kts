plugins {
    val kotlinVersion = "1.4.20"
    kotlin("multiplatform") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
}

repositories {
    jcenter()
    maven("https://dl.bintray.com/cy6ergn0m/uuid") {
        metadataSources { gradleMetadata() }
    }
    maven(url = "https://kotlin.bintray.com/kotlinx/") // kotlinx.datetime
}

kotlin {
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
    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
        withJava()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(json())
                implementation(dateTime())
                implementation("org.jetbrains.kotlinx.experimental:kotlinx-uuid-core:0.0.1")
                implementation(ktor("client-core"))
                implementation(coroutines("core"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
        val jsTest by getting {
            dependencies {

            }
        }
        val iosMain by getting {
            dependencies {
                implementation(ktor("client-ios"))
            }
        }
        val iosTest by getting {
            dependencies {

            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(ktor("server-cio"))
                implementation(ktor("auth"))

                implementation(exposed("core"))
                implementation(exposed("dao"))
                implementation(exposed("jdbc"))
                implementation(exposed("java-time")) // todo: kotlin-time


                implementation("org.jetbrains.kotlinx.experimental:ktor-server-uuid-jvm:0.0.1")
                implementation("org.jetbrains.kotlinx.experimental:exposed-uuid-jvm:0.0.1")

                runtimeOnly(logging())
                runtimeOnly(h2())
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(ktor("server-test-host"))
            }
        }
    }
}

tasks {
    val packForXcode by creating(Sync::class) {
        group = "build"
        val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
        val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
        val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
        val framework =
            kotlin.targets.getByName<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>(targetName).binaries.getFramework(mode)
        inputs.property("mode", mode)
        dependsOn(framework.linkTask)
        val targetDir = File(buildDir, "xcode-frameworks")
        from({ framework.outputDirectory })
        into(targetDir)
    }
    build { dependsOn(packForXcode) }
}

/**
 * [DateTime](https://github.com/Kotlin/kotlinx-datetime/releases)
 */
fun dateTime() = "org.jetbrains.kotlinx:kotlinx-datetime:0.1.0"

/**
 * [Serialization](https://github.com/Kotlin/kotlinx.serialization/releases/latest)
 */
fun json() = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1"

/**
 * [Exposed](https://github.com/JetBrains/Exposed/releases/latest)
 */
fun exposed(module: String) = "org.jetbrains.exposed:exposed-$module:0.28.1"

/**
 * [Logback](https://github.com/qos-ch/logback/releases)
 */
fun logging() = "ch.qos.logback:logback-classic:1.2.3"

/**
 * [H2](https://github.com/h2database/h2database/releases/latest)
 */
fun h2() = "com.h2database:h2:1.4.200"

/**
 * [Coroutines](https://github.com/Kotlin/kotlinx.coroutines/releases/latest)
 */
fun coroutines(module: String) = "org.jetbrains.kotlinx:kotlinx-coroutines-$module:1.4.1-native-mt"

/**
 * [Ktor](https://github.com/ktorio/ktor/releases/latest)
 */
fun ktor(module: String) = "io.ktor:ktor-$module:1.4.2"
