package kopama.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class KopamaProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = KopamaSymbolProcessor(
        codeGenerator = environment.codeGenerator,
        logger = environment.logger,
        options = environment.options
    )
}