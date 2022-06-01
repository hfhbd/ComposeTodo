import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.*

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("ComposeTodoDB") {
        packageName = "app.softwork.composetodo"
        deriveSchemaFromMigrations = true
        verifyMigrations = true
    }
}

kotlin {
    android()
    jvm("desktop")

    val xcf = XCFramework()
    fun KotlinNativeTarget.config() {
        compilations["main"].kotlinOptions.freeCompilerArgs += "-Xexport-kdoc"
        binaries {
            framework {
                xcf.add(this)
                export(projects.shared)
                export("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
                export("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
                export("app.softwork:kotlinx-uuid-core:0.0.14")
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
        val sqlDelight = "1.5.3"
        val ktor = "2.0.2"
        commonMain {
            dependencies {
                api(projects.shared)
                implementation("com.squareup.sqldelight:coroutines-extensions:$sqlDelight")
                implementation("app.softwork:kotlinx-uuid-sqldelight:0.0.14")

                api("io.ktor:ktor-client-logging:$ktor")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.2")
            }
        }

        val androidMain by getting {
            dependencies {
                api("com.squareup.sqldelight:android-driver:$sqlDelight")
                api("io.ktor:ktor-client-android:$ktor")
                api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
            }
        }

        val iosArm64Main by getting {
            dependencies {
                // Apache 2, https://github.com/ktorio/ktor/releases/latest
                implementation("io.ktor:ktor-client-darwin:$ktor")
                implementation("com.squareup.sqldelight:native-driver:$sqlDelight")
            }
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosArm64Main)
        }

        val iosArm64Test by getting

        val iosSimulatorArm64Test by getting {
            dependsOn(iosArm64Test)
        }
        val jsMain by getting {
            dependencies {
                api("com.squareup.sqldelight:sqljs-driver:$sqlDelight")
            }
        }
    }
}

android {
    namespace = "app.softwork.composetodo.clients"
    compileSdk = 31

    defaultConfig {
        minSdk = 26
        targetSdk = 31
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }
    }
}

tasks {
    val assembleXCFramework by this
    assemble {
        dependsOn(assembleXCFramework)
    }
}
