plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                useIR = true
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":client-core"))
            }
        }
        commonTest {
            dependencies {
            }
        }

        val jvmMain by getting {
            dependencies {
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
