import com.android.build.api.dsl.*
import org.gradle.api.*

@Suppress("MagicNumber")
fun <
    BuildFeaturesT : BuildFeatures,
    BuildTypeT : BuildType,
    DefaultConfigT : DefaultConfig,
    ProductFlavorT : ProductFlavor,
    ResourcesT : AndroidResources,
    InstallationT : Installation,
    > CommonExtension<
    BuildFeaturesT,
    BuildTypeT,
    DefaultConfigT,
    ProductFlavorT,
    ResourcesT,
    InstallationT,
    >.androidConfig() {
    compileSdk = TARGET_SDK

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

const val TARGET_SDK = 34
