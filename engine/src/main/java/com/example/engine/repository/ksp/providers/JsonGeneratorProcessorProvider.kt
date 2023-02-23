package com.example.engine.repository.ksp.providers

import com.example.engine.repository.ksp.processors.JsonGeneratorProcessor
import com.example.engine.repository.ksp.visitors.JsonGeneratorVisitor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessorProvider

@AutoService(SymbolProcessorProvider::class)
internal class JsonGeneratorProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment) =
        JsonGeneratorProcessor(
            JsonGeneratorVisitor(
                codeGenerator = environment.codeGenerator,
                logger = environment.logger
            )
        )
}