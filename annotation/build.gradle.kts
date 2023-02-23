plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

group = "org.example.annotation"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(kotlin("stdlib"))
}
