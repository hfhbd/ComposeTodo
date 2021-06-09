plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "0.5.0-build221"
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
                api(projects.shared)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("app.softwork:bootstrap-compose:0.0.10")
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
