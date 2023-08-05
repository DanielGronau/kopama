package kopama.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import kopama.Kopama

class KopamaSymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Kopama::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()

        if(!symbols.iterator().hasNext()) return emptyList()
        symbols.forEach { it.accept(KopamaVisitor(codeGenerator, logger), Unit) }
        return symbols.filterNot { it.validate() }.toList()
    }
}