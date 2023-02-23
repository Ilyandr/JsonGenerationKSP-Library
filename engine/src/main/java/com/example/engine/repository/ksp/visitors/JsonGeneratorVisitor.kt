package com.example.engine.repository.ksp.visitors

import com.example.engine.repository.utils.RandomGenerator.GENERATION_SUFFICE
import com.example.engine.repository.utils.RandomGenerator.GENERATION_SUFFICE_PACKAGE
import com.example.engine.repository.utils.RandomGenerator.generateJson
import com.example.engine.repository.utils.isDataClass
import com.example.engine.repository.utils.mapToFunctionData
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.ksp.writeTo
import java.util.Locale

internal class JsonGeneratorVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : KSVisitorVoid() {

    private val summableClassData: MutableMap<KSTypeReference, String> = mutableMapOf()
    private lateinit var resolver: Resolver

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

        if (!classDeclaration.isDataClass()) {
            logger.error(
                "@JsonGenerated cannot target non-data class ${classDeclaration.qualifiedName?.asString()}",
                classDeclaration
            )
            return
        }
        classDeclaration.getAllProperties()
            .forEach {
                it.accept(this, Unit)
            }
        if (summableClassData.isEmpty()) return

        requireClassFieldBuilder(classDeclaration).build()
            .writeTo(codeGenerator = codeGenerator, aggregating = false)
        summableClassData.clear()
    }

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
        function.parameters.forEach { it.accept(this, Unit) }
        requireFunctionFieldBuilder(function).build()
            .writeTo(codeGenerator = codeGenerator, aggregating = false)
    }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) =
        with(property) { summableClassData[type] = simpleName.asString() }

    infix fun setResolver(resolver: Resolver) {
        this.resolver = resolver
    }

    private infix fun requireClassFieldBuilder(classDeclaration: KSClassDeclaration) =
        with(classDeclaration) {
            FileSpec.builder(
                packageName = "${packageName.asString()}$GENERATION_SUFFICE_PACKAGE",
                fileName = this.qualifiedName?.getShortName() ?: throw NullPointerException(),
            ).apply {
                addProperty(
                    PropertySpec.builder(
                        "${simpleName.asString()}$GENERATION_SUFFICE",
                        String::class
                    ).addModifiers(KModifier.CONST).initializer(
                        "%S",
                        generateJson(resolver, summableClassData)
                    ).build()
                )
            }
        }

    private infix fun requireFunctionFieldBuilder(function: KSFunctionDeclaration) =
        with(function) {
            FileSpec.builder(
                packageName = "${packageName.asString()}$GENERATION_SUFFICE_PACKAGE",
                fileName = simpleName.asString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            ).apply {
                addProperty(
                    PropertySpec.builder(
                        "${simpleName.asString()}$GENERATION_SUFFICE",
                        String::class
                    ).addModifiers(KModifier.CONST).initializer(
                        "%S",
                        generateJson(resolver, function.mapToFunctionData())
                    ).build()
                )
            }
        }
}