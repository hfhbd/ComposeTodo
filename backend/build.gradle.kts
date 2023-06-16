plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("jib")
    id("license")
}

kotlin.jvmToolchain(17)

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

application.mainClass.set("app.softwork.composetodo.MainKt")

tasks.register<Jib>("jibGithub") {
    to.image.set("ghcr.io/hfhbd/composetodo:$version")
}

tasks.register<Jib>("jibGoogle") {
    to.image.set(
        providers.gradleProperty("project_id").zip(providers.gradleProperty("serviceName")) { projectId, serviceName ->
            "eu.gcr.io/$projectId/$serviceName:$version"
        }
    )
}

tasks.withType<Jib>().configureEach {
    from {
        image.set("eclipse-temurin:17-jre")

        platforms {
            register("arm") {
                architecture.set("arm64")
                os.set("linux")
            }
            register("amd64") {
                architecture.set("amd64")
                os.set("linux")
            }
        }
    }
}

licensee {
    allow("MIT")
    allow("EPL-1.0")
    allowUrl("https://raw.githubusercontent.com/auth0/jwks-rsa-java/master/LICENSE") // MIT
    allowUrl("https://raw.githubusercontent.com/auth0/java-jwt/master/LICENSE") // MIT
}
