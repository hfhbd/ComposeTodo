plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.serialization)
    implementation(libs.compose.gradlePlugin)
    implementation(libs.sqldelight.gradlePlugin)

    implementation(libs.android.gradlePlugin)
    implementation(libs.jib.gradlePlugin)

    implementation(libs.licensee.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
}
