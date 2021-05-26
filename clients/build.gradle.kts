plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "0.4.0-build211"
}

kotlin {
    jvm()
    ios()
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
                api(projects.shared)
                // Apache 2, https://github.com/ktorio/ktor/releases/latest
                api("io.ktor:ktor-client-core:main-133")
                // Apache 2, https://github.com/Kotlin/kotlinx.coroutines/releases/latest
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-native-mt")
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
                api(compose.foundation)
                api(compose.material)
            }
        }
        val jvmTest by getting {
            dependencies {

            }
        }

        val iosMain by getting {
            dependencies {
                // Apache 2, https://github.com/ktorio/ktor/releases/latest
                api("io.ktor:ktor-client-ios:main-133")
            }
        }
        val iosTest by getting
    }
}
