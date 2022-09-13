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
                cssSupport.enabled = true
            }
        }
    }
}

dependencies {
    implementation(projects.clients)

    implementation(compose.web.core)
    implementation("app.softwork:bootstrap-compose:0.1.10")
    implementation("app.softwork:routing-compose:0.2.8")
    implementation(npm("sql.js", "1.7.0"))

    implementation(devNpm("copy-webpack-plugin", "9.1.0"))
    implementation(devNpm("sass-loader", "^13.0.0"))
    implementation(devNpm("sass", "^1.52.1"))

    testImplementation(kotlin("test"))
}

licensee {
    allow("Apache-2.0")
    allowDependency("org.jetbrains.compose.web", "web-core-js", "1.2.0-alpha01-dev778")
    allowDependency("org.jetbrains.compose.web", "web-core", "1.2.0-alpha01-dev778")
    allowDependency("org.jetbrains.compose.web", "internal-web-core-runtime-js", "1.2.0-alpha01-dev778")
    allowDependency("org.jetbrains.compose.web", "internal-web-core-runtime", "1.2.0-alpha01-dev778")
}
