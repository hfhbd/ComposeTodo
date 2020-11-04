plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.4.10"
    `java-library`
}

repositories {
    jcenter()
}

dependencies {
    val coroutinesVersion: String by project
    val serializationVersion: String by project

    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-android", coroutinesVersion)
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-reactive", coroutinesVersion)
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", serializationVersion)

    testImplementation(kotlin("test-junit5"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        useIR = true
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}
