plugins {
    org.jetbrains.kotlin.js
    org.jetbrains.compose
    license
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
                    enabled = true
                }
            }
        }
    }
}

dependencies {
    implementation(projects.clients)

    implementation(compose.web.core)
    implementation("app.softwork:bootstrap-compose:0.1.13")
    implementation("app.softwork:routing-compose:0.2.11")

    implementation(npm("sql.js", "1.7.0"))
    implementation(devNpm("copy-webpack-plugin", "9.1.0"))

    testImplementation(kotlin("test"))
}
