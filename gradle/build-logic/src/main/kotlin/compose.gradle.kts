plugins {
    id("org.jetbrains.compose")
}

compose {
    kotlinCompilerPlugin.set(versionCatalogs.named("libs").findVersion("compose-compiler").get().requiredVersion)
}
