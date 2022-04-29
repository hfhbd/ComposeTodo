plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

dependencies {
    implementation(projects.composeClients)
    testImplementation(kotlin("test"))

    val ktor = "2.0.0"
    implementation("io.ktor:ktor-client-cio:$ktor")
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.3")
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "app.softwork.composetodo.MainKt"
    }
}
