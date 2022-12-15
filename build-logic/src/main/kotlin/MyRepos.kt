import org.gradle.api.*
import org.gradle.api.initialization.*
import org.gradle.kotlin.dsl.*

class MyRepos : Plugin<Settings> {
    override fun apply(settings: Settings) {
        settings.dependencyResolutionManagement {
            repositories {
                mavenCentral()
                google()
                maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
            }
        }
    }
}
