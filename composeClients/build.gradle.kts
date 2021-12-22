plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    android()
    jvm("desktop")

    js(IR) {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.clients)
                api(compose.runtime)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val desktopMain by getting {
            dependencies {
                api(compose.foundation)
                api(compose.material)
            }
        }

        val androidMain by getting {
            dependencies {
                api(compose.foundation)
                api(compose.material)
                implementation("io.github.vanpra.compose-material-dialogs:datetime:0.6.2")
            }
        }

        val jsMain by getting {
            dependencies {
                api(compose.web.core)
                api("app.softwork:bootstrap-compose:0.0.49")
            }
        }
    }
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 26
        targetSdk = 31
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }
    }
}
