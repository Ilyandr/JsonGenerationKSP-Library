package com.example.engine.repository.utils

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import java.util.AbstractSequentialList


internal const val EMPTY_STRING = ""

internal infix fun Resolver.isCollectionType(type: KSType) = with(
    getClassDeclarationByName(type.toClassName().reflectionName())?.superTypes?.first()
        ?.resolve()?.toClassName().toString()
) {
    contains(Collection::class.java.simpleName) || this == AbstractSequentialList::class.qualifiedName
}

internal infix fun Resolver.isMapType(type: KSType) = with(
    getClassDeclarationByName(
        type.toClassName().reflectionName()
    )?.toClassName()?.simpleName.toString()
) {
    contains(Map::class.simpleName.toString())
}

internal fun KSTypeReference.requireAllGenericsType() =
    element?.typeArguments?.map { it.type?.resolve() ?: requireTypeCastException() }

internal fun KSType.requireAllGenericsType() = arguments.map { it.type?.resolve() ?: requireTypeCastException() }

internal fun KSType.requireGenericClassName() = if (arguments.isNotEmpty()) {
    arguments.first().toTypeName().toString()
} else {
    toClassName().toString()
}

internal fun KSType.requireGenericClass() = if (arguments.isNotEmpty()) {
    arguments.first().type?.resolve()
} else {
    this
}

internal fun requireTypeCastException(): Nothing =
    throw TypeCastException("Error type cast exception")

internal fun requireNullPointerException(): Nothing =
    throw NullPointerException("Error, object is null")

internal fun KSFunctionDeclaration.mapToFunctionData() =
    mutableMapOf<KSTypeReference, String>().apply {
        parameters.forEach { singleParameter ->
            put(singleParameter.type, singleParameter.name?.asString() ?: EMPTY_STRING)
        }
    }

internal fun Int.isEven() = this % 2 == 0

internal fun KSClassDeclaration.isDataClass() = modifiers.contains(Modifier.DATA)