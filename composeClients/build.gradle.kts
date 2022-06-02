plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    android()
    jvm("desktop")

    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
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
                implementation("io.github.vanpra.compose-material-dialogs:datetime:0.7.2")
            }
        }

        val jsMain by getting {
            dependencies {
                api(compose.web.core)
                api("app.softwork:bootstrap-compose:0.0.52")
            }
        }
    }
}

android {
    namespace = "app.softwork.composetodo.composeclients"
    compileSdk = 31

    defaultConfig {
        minSdk = 26
        targetSdk = 31
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
