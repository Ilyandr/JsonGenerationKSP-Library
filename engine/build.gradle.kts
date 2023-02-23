plugins {
    id("java-library")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.jvm")
}

group = "org.example.engine"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {

    // Modules
    implementation(project(":annotation"))
    implementation(kotlin("stdlib"))

    // KSP (compiling processor)
    val kspVersion = "1.8.10-1.0.9"
    val autoServiceVersion = "1.0.0"
    val autoServiceAnnotationsVersion = "1.0"
    ksp("dev.zacsweers.autoservice:auto-service-ksp:$autoServiceVersion")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    implementation("com.google.auto.service:auto-service-annotations:$autoServiceAnnotationsVersion")

    // Poet (generation source code)
    val poetVersion = "1.12.0"
    implementation("com.squareup:kotlinpoet:$poetVersion")
    implementation("com.squareup:kotlinpoet-ksp:$poetVersion")

    // Random
    val commonsTextVersion = "1.6"
    implementation("org.apache.commons:commons-text:$commonsTextVersion")
}
