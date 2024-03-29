plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("application")
    id("com.google.cloud.tools.jib")
    id("license")
}

application.mainClass.set("app.softwork.composetodo.MainKt")

kotlin.jvmToolchain(21)

jib {
    from {
        image = "eclipse-temurin:21-jre-alpine"

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

    val registry = providers.gradleProperty("registry").orNull
    to.image = when (registry) {
        "GitHub" -> "ghcr.io/hfhbd/composetodo:$version"
        "Google" -> {
            val project_id = providers.gradleProperty("project_id").get()
            val service_name = providers.gradleProperty("service_name").get()
            "europe-west4-docker.pkg.dev/$project_id/composetodo-repo/$service_name:$version"
        }

        else -> return@jib
    }
}

dependencies {
    implementation(projects.shared)

    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.auth.jwt)
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
