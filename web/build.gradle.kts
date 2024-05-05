plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
    id("license")
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
            commonWebpackConfig {
                scssSupport {
                    enabled.set(true)
                }
            }
        }
    }

    sourceSets {
        named("jsMain") {
            dependencies {
                implementation(projects.clients)

                implementation(compose.html.core)
                implementation(libs.bootstrapCompose)
                implementation(libs.routingCompose)

                implementation(libs.sqldelight.sqljsDriver)
                implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.0.0"))
                implementation(npm("sql.js", "1.8.0"))
                implementation(devNpm("copy-webpack-plugin", "9.1.0"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
