package com.example.engine.repository.ksp.processors

import com.example.annotation.source.JsonGenerated
import com.example.engine.repository.ksp.visitors.JsonGeneratorVisitor
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.validate

internal class JsonGeneratorProcessor(
    private val visitor: JsonGeneratorVisitor
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(
            JsonGenerated::class.qualifiedName ?: return emptyList()
        )
        visitor setResolver resolver
        val unableToProcess = symbols.filterNot { it.validate() }
        symbols.filter { it.validate() }.forEach { it.accept(visitor, Unit) }
        return unableToProcess.toList()
    }
}
