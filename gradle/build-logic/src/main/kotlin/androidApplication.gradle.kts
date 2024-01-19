plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    androidConfig()

    defaultConfig {
        targetSdk = TARGET_SDK
    }
}
