plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    application
}

application {
    mainClass.set("app.softwork.composetodo.MainKt")
}

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        // Apache 2, https://github.com/ktorio/ktor/releases/latest
        val ktorVersion = "1.5.4"

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-cio:$ktorVersion")
                implementation("io.ktor:ktor-auth-jwt:$ktorVersion")

                // Apache 2, https://github.com/hfhbd/RateLimit/releases/latest
                implementation("app.softwork:ratelimit:0.0.8")

                // Apache 2, https://github.com/hfhbd/cloudkitclient/releases/latest
                implementation("app.softwork:cloudkitclient-core:0.0.7")

                // EPL 1.0, https://github.com/qos-ch/logback/releases
                runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("io.ktor:ktor-server-test-host:$ktorVersion")
                implementation("app.softwork:cloudkitclient-testing:0.0.7")
            }
        }
    }
}

// only necessary until https://youtrack.jetbrains.com/issue/KT-37964 is resolved
distributions {
    main {
        contents {
            from("$buildDir/libs") {
                exclude(project.name)
                rename("${project.name}-jvm", project.name)
                into("lib")
            }
        }
    }
}
