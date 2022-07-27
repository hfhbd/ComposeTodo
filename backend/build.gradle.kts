plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.google.cloud.tools.jib")
    id("app.cash.licensee")
}

kotlin.target.compilations.all {
    kotlinOptions {
        jvmTarget = "17"
    }
}

jib {
    val registry: String? by project
    to.image = when (registry) {
        "GitHub" -> "ghcr.io/hfhbd/composetodo:$version"
        "Google" -> {
            val project_id: String by project
            val service_name: String by project
            "eu.gcr.io/$project_id/$service_name:$version"
        }

        else -> return@jib
    }

    from {
        image = "eclipse-temurin:17-jre"

        platforms {
            platform {
                architecture = "arm64"
                os = "linux"
            }
            platform {
                architecture = "amd64"
                os = "linux"
            }
        }
    }
}

dependencies {
    implementation(projects.shared)

    // Apache 2, https://github.com/ktorio/ktor/releases/latest
    val ktor = "2.0.3"

    implementation("io.ktor:ktor-server-cio:$ktor")
    implementation("io.ktor:ktor-server-cors:$ktor")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor")
    implementation("io.ktor:ktor-server-resources:$ktor")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
    implementation("io.ktor:ktor-server-forwarded-header:$ktor")

    // Apache 2, https://github.com/hfhbd/cloudkitclient/releases/latest
    implementation("app.softwork:cloudkitclient-core:0.1.0")

    // EPL 1.0, https://github.com/qos-ch/logback/releases
    runtimeOnly("ch.qos.logback:logback-classic:1.2.11")

    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host:$ktor")
    implementation("app.softwork:cloudkitclient-testing:0.1.0")
}

licensee {
    allow("Apache-2.0")
    allow("MIT")
    allow("EPL-1.0")
    allowUrl("https://raw.githubusercontent.com/auth0/jwks-rsa-java/master/LICENSE") // MIT
    allowUrl("https://raw.githubusercontent.com/auth0/java-jwt/master/LICENSE") // MIT
}
