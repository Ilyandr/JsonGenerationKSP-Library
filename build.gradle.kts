plugins {
    val kotlinVersion = "1.8.10"
    val androidLibraryVersion = "7.4.1"
    val kspVersion = "1.8.10-1.0.9"
    id("com.google.devtools.ksp").version(kspVersion).apply(true)
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("com.android.library") version androidLibraryVersion apply false
    id("org.jetbrains.kotlin.jvm") version kotlinVersion apply false
}

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
    dependencies {
        val kotlinVersion = "1.8.10"
        val gradleVersion = "7.4.1"
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.android.tools.build:gradle:$gradleVersion")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}