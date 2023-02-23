@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

sourceSets.configureEach {
    java {
        srcDir("${buildDir.absolutePath}/generated/ksp/")
    }
}

android {
    namespace = "com.example.kspexample"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.kspexample"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    viewBinding {
        enable = true
    }
}

dependencies {

    ksp(project(":engine"))
    implementation(project(":annotation"))

    // Gson
    val gsonVersion = "2.10"
    implementation("com.google.code.gson:gson:$gsonVersion")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}