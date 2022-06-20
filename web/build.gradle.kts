plugins {
    kotlin("js")
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
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
}

dependencies {
    implementation(projects.clients)

    implementation(compose.web.core)
    implementation("app.softwork:bootstrap-compose:0.1.5")
    implementation("app.softwork:routing-compose:0.2.4")

    implementation(devNpm("copy-webpack-plugin", "9.1.0"))
    implementation(devNpm("sass-loader", "^13.0.0"))
    implementation(devNpm("sass", "^1.52.1"))

    testImplementation(kotlin("test"))
}
