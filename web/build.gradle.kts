plugins {
    id("org.jetbrains.kotlin.js")
    id("org.jetbrains.compose")
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
}

dependencies {
    implementation(projects.clients)

    implementation(compose.html.core)
    implementation(libs.bootstrapCompose)
    implementation(libs.routingCompose)

    implementation(npm("sql.js", "1.7.0"))
    implementation(devNpm("copy-webpack-plugin", "9.1.0"))

    testImplementation(kotlin("test"))
}
