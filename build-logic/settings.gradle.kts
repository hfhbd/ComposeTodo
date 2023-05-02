dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    versionCatalogs.register("libs") {
        from(files("../gradle/libs.versions.toml"))
    }
}

rootProject.name = "build-logic"
