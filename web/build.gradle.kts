plugins {
    kotlin("js")
    id("org.jetbrains.compose")
    id("app.cash.licensee")
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
    implementation("app.softwork:bootstrap-compose:0.1.11")
    implementation("app.softwork:routing-compose:0.2.9")

    implementation(npm("sql.js", "1.7.0"))
    implementation(devNpm("copy-webpack-plugin", "9.1.0"))

    testImplementation(kotlin("test"))
}

licensee {
    allow("Apache-2.0")
}
