buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        val kotlinVersion: String by project
        classpath("com.android.tools.build", "gradle", "4.2.0-alpha07")
        classpath(kotlin("gradle-plugin:$kotlinVersion"))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://kotlin.bintray.com/kotlinx")
    }
}
