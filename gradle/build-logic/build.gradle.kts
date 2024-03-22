plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.plugins.kotlin.jvm.toDep())
    implementation(libs.plugins.kotlin.serialization.toDep())
    implementation(libs.plugins.kotlin.compose.toDep())
    implementation(libs.plugins.compose.toDep())
    implementation(libs.plugins.sqldelight.toDep())

    implementation(libs.plugins.android.toDep())
    implementation(libs.plugins.jib.toDep())

    implementation(libs.plugins.licensee.toDep())
    implementation(libs.plugins.detekt.toDep())
}

fun Provider<PluginDependency>.toDep() = map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}
