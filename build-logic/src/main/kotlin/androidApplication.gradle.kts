plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    androidConfig()

    defaultConfig {
        targetSdk = TARGET_SDK
    }
}
