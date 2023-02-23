package com.example.annotation.source

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class JsonGenerated(
    val enabled: Boolean = true,
    val generationType: GenerationType = GenerationType.RANDOM,
    val useZeroValues: Boolean = false,
    val minSizeArray: Int = 1,
    val maxSizeArray: Int = 3,
)

enum class GenerationType {
    RANDOM, DEFAULT, COMBINED
}