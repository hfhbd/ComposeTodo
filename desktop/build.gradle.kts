plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.clients)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:1.6.2")
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting {
            dependencies {
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "app.softwork.composetodo.MainKt"
    }
}
