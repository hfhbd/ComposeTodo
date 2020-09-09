buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        val kotlinVersion: String by project
        classpath("com.android.tools.build", "gradle", "4.0.1")
        classpath(kotlin("gradle-plugin", kotlinVersion))
    }
}
