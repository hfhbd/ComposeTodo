plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

dependencies {
    implementation(projects.composeClients)
    testImplementation(kotlin("test"))

    val ktor = "2.0.2"
    implementation("io.ktor:ktor-client-cio:$ktor")
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.3")
    implementation(compose.desktop.currentOs)
}

kotlin.target.compilations.all {
    kotlinOptions.jvmTarget = "1.8"
}

compose.desktop {
    application {
        mainClass = "app.softwork.composetodo.MainKt"
    }
}
