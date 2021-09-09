plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm()

    iosArm64 {
        binaries {
            framework {
                baseName = "shared"
                export(projects.shared)
                // Export transitively.
                transitiveExport = true
            }
        }
    }

    js(IR) {
        browser {
            binaries.library()
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.shared)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

       val jvmMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
            }
        }
        val jvmTest by getting {
            dependencies {

            }
        }
    }
}
