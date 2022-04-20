plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application
}

application {
    mainClass.set("app.softwork.composetodo.MainKt")
}


dependencies {
    implementation(projects.shared)

    // Apache 2, https://github.com/ktorio/ktor/releases/latest
    val ktor = "2.0.0"

    implementation("io.ktor:ktor-server-cio:$ktor")
    implementation("io.ktor:ktor-server-cors:$ktor")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor")
    implementation("io.ktor:ktor-server-resources:$ktor")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")

    // Apache 2, https://github.com/hfhbd/cloudkitclient/releases/latest
    implementation("app.softwork:cloudkitclient-core:0.1.0")

    // EPL 1.0, https://github.com/qos-ch/logback/releases
    runtimeOnly("ch.qos.logback:logback-classic:1.2.11")

    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host:$ktor")
    implementation("app.softwork:cloudkitclient-testing:0.1.0")
}
