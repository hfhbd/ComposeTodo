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
    val ktorVersion = "1.6.5"

    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")

    // Apache 2, https://github.com/hfhbd/RateLimit/releases/latest
    implementation("app.softwork:ratelimit:0.0.10")

    // Apache 2, https://github.com/hfhbd/cloudkitclient/releases/latest
    implementation("app.softwork:cloudkitclient-core:0.0.8")

    // EPL 1.0, https://github.com/qos-ch/logback/releases
    runtimeOnly("ch.qos.logback:logback-classic:1.2.6")


    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    implementation("app.softwork:cloudkitclient-testing:0.0.8")
}
