plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
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
                api(projects.composeClients)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("app.softwork:routing-compose:0.1.8")
                implementation(compose.web.core)
                implementation(compose.runtime)
                implementation("com.squareup.sqldelight:sqljs-driver:1.5.3")
                implementation(devNpm("copy-webpack-plugin", "9.1.0"))
            }
        }

        val jsTest by getting {
            dependencies {

            }
        }
    }
}
