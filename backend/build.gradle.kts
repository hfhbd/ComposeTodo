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
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                useIR = true
            }
        }
        withJava()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared"))
            }
        }
        // Apache 2, https://github.com/ktorio/ktor/releases/latest
        val ktorVersion = "1.5.2"

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-cio:$ktorVersion")
                implementation("io.ktor:ktor-auth:$ktorVersion")

                // Apache 2, https://github.com/JetBrains/Exposed/releases/latest
                val exposedVersion = "0.29.1"
                implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
                // todo: kotlin-time
                implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")

                // Apache 2, https://github.com/cy6erGn0m/kotlinx-uuid/releases
                implementation("org.jetbrains.kotlinx.experimental:ktor-server-uuid-jvm:0.0.3")
                implementation("org.jetbrains.kotlinx.experimental:exposed-uuid-jvm:0.0.3")

                // EPL 1.0, https://github.com/qos-ch/logback/releases
                runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
                // MPL 2.0 or EPL 1.0, https://github.com/h2database/h2database/releases/latest
                runtimeOnly("com.h2database:h2:1.4.200")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("io.ktor:ktor-server-test-host:$ktorVersion")
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
