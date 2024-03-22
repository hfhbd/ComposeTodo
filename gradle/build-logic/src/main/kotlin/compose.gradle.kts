import gradle.kotlin.dsl.accessors._45f0809d393e9cd55e933de1d9208596.*

plugins {
    id("org.jetbrains.compose")
}

compose {
    kotlinCompilerPlugin.set(versionCatalogs.named("libs").findVersion("compose-compiler").get().requiredVersion)
}
