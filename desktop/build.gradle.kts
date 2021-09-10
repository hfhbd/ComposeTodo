plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

dependencies {
    implementation(projects.composeClients)
    testImplementation(kotlin("test"))

    implementation("io.ktor:ktor-client-cio:1.6.3")
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.0")
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "app.softwork.composetodo.MainKt"
    }
}
