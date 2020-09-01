plugins {
    `java-library`
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.4.0"
}

repositories {
    jcenter()
    maven("https://kotlin.bintray.com/kotlinx")
}

dependencies {
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-android", "1.3.9")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-core", "1.0.0-RC")

    testImplementation(kotlin("test-junit5"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        useIR = true
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}
