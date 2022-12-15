plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
    implementation("org.jetbrains.kotlin:kotlin-serialization:1.7.20")
    implementation("org.jetbrains.compose:compose-gradle-plugin:1.2.2")
    implementation("app.cash.sqldelight:gradle-plugin:2.0.0-alpha04")

    implementation("com.android.application:com.android.application.gradle.plugin:7.3.1")
    implementation("com.google.cloud.tools:jib-gradle-plugin:3.3.1")

    implementation("app.cash.licensee:licensee-gradle-plugin:1.6.0")
    implementation("org.jetbrains.kotlinx:kover:0.6.1")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.22.0")
}

gradlePlugin {
    plugins {
        create("MyRepos") {
            id = "MyRepos"
            implementationClass = "MyRepos"
        }
    }
}
