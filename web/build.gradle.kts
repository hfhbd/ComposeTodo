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
                api(projects.clients) // try composeClients
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("app.softwork:bootstrap-compose:0.0.41")
                implementation("app.softwork:routing-compose:0.0.28")
                implementation(compose.web.core)
                implementation(compose.runtime)
            }
        }

        val jsTest by getting {
            dependencies {

            }
        }
    }
}
