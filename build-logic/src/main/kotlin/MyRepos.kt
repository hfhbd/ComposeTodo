import org.gradle.api.*
import org.gradle.api.initialization.*
import org.gradle.api.initialization.resolve.*
import org.gradle.kotlin.dsl.*

class MyRepos : Plugin<Settings> {
    override fun apply(settings: Settings) {
        settings.dependencyResolutionManagement {
            repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
            repositories {
                mavenCentral()
                google()
                maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
            }
        }
    }
}
