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

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared"))
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

                implementation("org.jetbrains.kotlinx.experimental:ktor-server-uuid-jvm:0.0.2")
                implementation("org.jetbrains.kotlinx.experimental:exposed-uuid-jvm:0.0.2")

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

/**
 * [Ktor](https://github.com/ktorio/ktor/releases/latest)
 */
fun ktor(module: String) = "io.ktor:ktor-$module:1.4.3"

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