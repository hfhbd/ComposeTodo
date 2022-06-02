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
    implementation(projects.composeClients)
    implementation("app.softwork:routing-compose:0.1.9-dev670")
    implementation(compose.web.core)
    implementation(compose.runtime)
    implementation(devNpm("copy-webpack-plugin", "9.1.0"))

    testImplementation(kotlin("test"))
}
