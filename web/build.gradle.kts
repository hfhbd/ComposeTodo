import org.jetbrains.kotlin.gradle.dsl.*

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
                    enabled.set(true)
                }
            }
        }
    }
}

dependencies {
    implementation(projects.clients)

    implementation(compose.html.core)
    implementation("app.softwork:bootstrap-compose:0.1.15")
    implementation("app.softwork:routing-compose:0.2.12")

    implementation(npm("sql.js", "1.7.0"))
    implementation(devNpm("copy-webpack-plugin", "9.1.0"))

    testImplementation(kotlin("test"))
}
