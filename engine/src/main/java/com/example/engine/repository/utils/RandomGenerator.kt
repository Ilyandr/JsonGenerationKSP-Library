package com.example.engine.repository.utils

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference
import com.squareup.kotlinpoet.ksp.toClassName
import org.apache.commons.lang3.RandomStringUtils

internal object RandomGenerator {

    const val GENERATION_SUFFICE = "Json"
    const val GENERATION_SUFFICE_PACKAGE = ".generated"
    private const val CHARACTERS_MAX_LENGTH = 16
    private val range = (0..3000)


    fun generateJson(resolver: Resolver, properties: Map<KSTypeReference, String?>) =
        with(StringBuilder()) {
            append("{")
            properties.forEach { (key, propertyName) ->
                append(
                    when (key.resolve().toClassName().toString()) {
                        String::class.qualifiedName -> generateString(propertyName)
                        Int::class.qualifiedName -> generateInt(propertyName)
                        Boolean::class.qualifiedName -> generateBoolean(propertyName)
                        Float::class.qualifiedName -> generateFloat(propertyName)
                        Double::class.qualifiedName -> generateDouble(propertyName)
                        else -> {
                            if (resolver isMapType key.resolve()) {
                                generateMapObject(
                                    resolver = resolver,
                                    name = propertyName ?: requireNullPointerException(),
                                    type = key
                                )
                            } else if (resolver.isCollectionType(key.resolve())) {
                                generateCollection(
                                    resolver,
                                    propertyName,
                                    key.resolve(),
                                    true
                                )
                            } else {
                                generateObject(resolver, propertyName, key.resolve())
                            }
                        }
                    }
                )
                append(",")
            }
            append("}"); formatData()
        }

    private fun generateCollectionGenericObject(
        resolver: Resolver,
        propertyName: String?,
        type: KSType,
        withCloseSymbol: Boolean
    ) =
        with(StringBuilder()) {
            append(
                when (type.requireGenericClassName()) {
                    String::class.qualifiedName -> generateString(null)
                    Int::class.qualifiedName -> generateInt(null)
                    Float::class.qualifiedName -> generateFloat(null)
                    Boolean::class.qualifiedName -> generateBoolean(null)
                    Double::class.qualifiedName -> generateDouble(null)
                    else -> {
                        val genericType = type.requireGenericClass() ?: requireTypeCastException()
                        val map = generateMapObject(
                            resolver = resolver,
                            propertyName,
                            type = genericType
                        )
                        if (map.isNotEmpty()) map
                        else if (resolver.isCollectionType(genericType)) {
                            generateCollection(
                                resolver,
                                propertyName,
                                genericType,
                                false
                            )
                        } else {
                            generateObject(resolver, null, genericType)
                        }
                    }
                }
            )
            if (withCloseSymbol) append("}")
            formatData()
        }

    private fun generateObject(
        resolver: Resolver,
        name: String?,
        type: KSType
    ): String {
        return "${if (!name.isNullOrEmpty()) "$name:" else EMPTY_STRING}${
            generateJson(resolver, mutableMapOf<KSTypeReference, String>().apply {
                resolver.getClassDeclarationByName(type.toClassName().reflectionName())
                    ?.getAllProperties()
                    ?.forEach { singleValue ->
                        with(singleValue) {
                            this@apply[this.type] = simpleName.asString()
                        }
                    }
            }
            )
        }"
    }

    private fun generateMapObject(
        resolver: Resolver,
        name: String,
        type: KSTypeReference
    ): String {
        return type.requireAllGenericsType()?.run {
            val stringBuilder = StringBuilder()
            stringBuilder.append("\"$name\":{")
            mutableMapOf<KSTypeReference, String>().apply {
                this@run.forEach { singleArg ->
                    stringBuilder.append(
                        "${
                            generateCollectionGenericObject(
                                resolver,
                                null,
                                singleArg,
                                true
                            )
                        }:"
                    )
                }
            }
            stringBuilder.toString()
        } ?: requireNullPointerException()
    }

    private fun generateMapObject(
        resolver: Resolver,
        name: String?,
        type: KSType
    ): String {
        return type.requireAllGenericsType().run {
            if (isEmpty()) return EMPTY_STRING
            val stringBuilder = StringBuilder()
            stringBuilder.append("\"$name\":{")
            mutableMapOf<KSTypeReference, String>().apply {
                this@run.forEach { singleArg ->
                    stringBuilder.append(
                        generateCollectionGenericObject(
                            resolver,
                            null,
                            singleArg,
                            true
                        )
                    )
                }
            }
            stringBuilder.toString()
        }
    }

    private fun generateString(propertyName: String?) =
        "${if (!propertyName.isNullOrEmpty()) "\"$propertyName\":" else EMPTY_STRING}\"${
            RandomStringUtils.randomAlphanumeric(
                CHARACTERS_MAX_LENGTH
            )
        }\""

    private fun generateInt(propertyName: String?) =
        "${if (!propertyName.isNullOrEmpty()) "\"$propertyName\":" else EMPTY_STRING}${
            range.shuffled().last()
        }"

    private fun generateBoolean(propertyName: String?) =
        "${if (!propertyName.isNullOrEmpty()) "\"$propertyName\":" else EMPTY_STRING}${
            range.shuffled().last().isEven()
        }"

    private fun generateDouble(propertyName: String?) =
        "${if (!propertyName.isNullOrEmpty()) "\"$propertyName\":" else EMPTY_STRING}${
            range.shuffled().last().toDouble()
        }"

    private fun generateFloat(propertyName: String?) =
        "${if (!propertyName.isNullOrEmpty()) "\"$propertyName\":" else EMPTY_STRING}${
            range.shuffled().last().toFloat()
        }"

    private fun generateCollection(
        resolver: Resolver,
        propertyName: String?,
        type: KSType,
        useBaseName: Boolean
    ): String {
        return "${if (useBaseName) "\"$propertyName\"" else EMPTY_STRING}:${
            mutableListOf<String>().apply {
                if (useBaseName) {
                    for (i in 0..2) {
                        add(generateCollectionGenericObject(resolver, propertyName, type, false))
                    }
                }
            }
        }"
    }

    private fun StringBuilder.formatData() = toString()
        .replace("\"\"", "\"")
        .replace(",}", "}")
        .replace(":}", "}")
        .replace("]]", "]")
        .replace("[[", "[")
        .replace("[[", "[")
        .replace("}:", ":")
        .replace("[:[", "[")
}