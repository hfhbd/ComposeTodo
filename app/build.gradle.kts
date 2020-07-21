plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.example.jetpacknews"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = listOf("-Xallow-result-return-type", "-XXLanguage:+NonParenthesizedAnnotationsOnFunctionalTypes")
    }

    composeOptions {
        kotlinCompilerVersion = "1.3.70-dev-withExperimentalGoogleExtensions-20200424"
        val composeVersion: String by project
        kotlinCompilerExtensionVersion = composeVersion
    }

    buildTypes {
        release {
            isMinifyEnabled = true
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("androidx.core:core-ktx:1.3.0")
    implementation("androidx.appcompat:appcompat:1.1.0")

    implementation(compose("runtime"))
    implementation(composeUI("core"))
    implementation(composeUI("layout"))
    implementation(composeUI("material"))
    implementation(composeUI("material-icons-extended"))
    implementation(composeUI("foundation"))
    implementation(composeUI("animation"))
    implementation(composeUI("tooling"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.8")
    implementation("androidx.activity:activity-ktx:1.1.0")

    testImplementation(kotlin("test-junit"))
    androidTestImplementation("androidx.test:rules:1.2.0")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation(composeUI("test"))
}

fun <T> NamedDomainObjectContainer<T>.release(action: T.() -> Unit): T =
    getByName("release", action)

fun compose(module: String): String {
    val composeVersion: String by project
    return "androidx.compose:compose-$module:$composeVersion"
}

fun composeUI(module: String): String {
    val composeVersion: String by project
    return "androidx.ui:ui-$module:$composeVersion"
}
