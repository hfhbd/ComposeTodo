import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.*

plugins {
    id("androidLibrary")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.compose")
    id("app.cash.sqldelight")
}

sqldelight {
    databases.register("ComposeTodoDB") {
        packageName.set("app.softwork.composetodo")
        deriveSchemaFromMigrations.set(true)
        verifyMigrations.set(true)
    }
}

kotlin {
    jvmToolchain(8)
    
    android()
    jvm("desktop")

    val xcf = XCFramework()
    fun KotlinNativeTarget.config() {
        binaries {
            framework {
                xcf.add(this)
                export(projects.shared)
                export(libs.coroutines.core)
                export(libs.datetime)
                export(libs.uuid.core)
                embedBitcodeMode.set(BitcodeEmbeddingMode.DISABLE)
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
        commonMain {
            dependencies {
                api(projects.shared)
                implementation(libs.sqldelight.coroutinesExtensions)

                api(libs.ktor.client.logging)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.coroutines.test)
            }
        }

        named("androidMain") {
            dependencies {
                api(libs.sqldelight.androidDriver)
                api(libs.ktor.client.android)
                api(libs.androidx.viewmodel.lifecycle)
            }
        }

        val iosMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.ktor.client.darwin)
                implementation(libs.sqldelight.nativeDriver)
            }
        }
        val iosTest by creating {
            dependsOn(commonTest.get())
        }
        
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm64Test by getting {
            dependsOn(iosTest)
        }
        
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
        }

        val jsMain by getting {
            dependencies {
                api(libs.sqldelight.sqljsDriver)
            }
        }
    }
}

android {
    namespace = "app.softwork.composetodo.clients"
}

tasks {
    val assembleXCFramework by existing
    assemble {
        dependsOn(assembleXCFramework)
    }
}

// https://youtrack.jetbrains.com/issue/KT-55751
configurations {
    val myAttribute = Attribute.of("dummy.attribute", String::class.java)

    named("debugFrameworkIosFat") { attributes.attribute(myAttribute, "dummy-value") }
    named("releaseFrameworkIosFat") { attributes.attribute(myAttribute, "dummy-value") }
}