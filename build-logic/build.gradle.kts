plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
    implementation("org.jetbrains.kotlin:kotlin-serialization:1.8.20")
    implementation("org.jetbrains.compose:compose-gradle-plugin:1.3.1")
    implementation("app.cash.sqldelight:gradle-plugin:2.0.0-alpha05")

    implementation("com.android.tools.build:gradle:7.4.2")
    implementation("com.google.cloud.tools:jib-gradle-plugin:3.3.1")

    implementation("app.cash.licensee:licensee-gradle-plugin:1.6.0")
    implementation("org.jetbrains.kotlinx:kover:0.6.1")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.22.0")
}
