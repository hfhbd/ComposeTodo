plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.google.cloud.tools.jib")
    id("license")
}

kotlin.jvmToolchain(17)

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

    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.resources)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.forwarded.header)

    implementation(libs.cloudkit.core)
    implementation(libs.cloudkit.testing)

    runtimeOnly(libs.logback)

    testImplementation(kotlin("test"))
    testImplementation(libs.ktor.server.test.host)
}

licensee {
    allow("MIT")
    allow("EPL-1.0")
    allowUrl("https://raw.githubusercontent.com/auth0/jwks-rsa-java/master/LICENSE") // MIT
    allowUrl("https://raw.githubusercontent.com/auth0/java-jwt/master/LICENSE") // MIT
}
