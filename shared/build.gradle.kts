plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    js(IR) {
        browser()
    }
    ios()
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                useIR = true
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                // Apache 2, https://github.com/Kotlin/kotlinx.serialization/releases/latest
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.0")

                // Apache 2, https://github.com/Kotlin/kotlinx-datetime/releases
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.2.0")

                // Apache 2, https://github.com/cy6erGn0m/kotlinx-uuid/releases
                api("org.jetbrains.kotlinx.experimental:kotlinx-uuid-core:0.0.3")
            }
        }
        commonTest {
            dependencies {
                api(kotlin("test-common"))
                api(kotlin("test-annotations-common"))
            }
        }
    }
}
