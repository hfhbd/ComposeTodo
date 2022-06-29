plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

dependencies {
    implementation(projects.composeClients)

    val ktor = "2.0.3"
    implementation("io.ktor:ktor-client-cio:$ktor")
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.3")
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.3")

    testImplementation(kotlin("test"))
}

kotlin.target.compilations.all {
    kotlinOptions {
        freeCompilerArgs += "-Xlambdas=indy"
        jvmTarget = "17"
    }
}

compose.desktop {
    application {
        mainClass = "app.softwork.composetodo.MainKt"
    }
}
