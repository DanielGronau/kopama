package kopama.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import java.util.*

class KopamaVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : KSVisitorVoid() {

    private val patternClassName = ClassName("kopama", "Pattern")

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val shortName = classDeclaration.simpleName.getShortName()

        logger.warn("found $shortName")

        if (Modifier.DATA !in classDeclaration.modifiers) {
            logger.error("Can't generate pattern, <$shortName> isn't a data class")
        }

        val parameters = classDeclaration.primaryConstructor!!.parameters
        val funSpec = patternFunction(shortName, parameters, classDeclaration)

        val fileSpec = FileSpec.builder(
            packageName = classDeclaration.packageName.asString(),
            fileName = shortName.decap() + "Pattern"
        ).addFunction(funSpec).addImport("kopama", "any").build()

        fileSpec.writeTo(codeGenerator, false)
    }

    private fun patternFunction(
        shortName: String,
        parameters: List<KSValueParameter>,
        classDeclaration: KSClassDeclaration
    ) = FunSpec.builder(functionName(shortName))
        .addParameters(parameters.map { param ->
            ParameterSpec.builder(
                name = param.name!!.getShortName(),
                type = patternClassName.parameterizedBy(param.type.toTypeName())
            ).defaultValue("any")
                .build()
        })
        .returns(patternClassName.parameterizedBy(classDeclaration.toClassName().copy(nullable = true)))
        .beginControlFlow("return")
        .beginControlFlow("when(it)")
        .addCode("null -> false\n")
        .addCode("else -> %L", parameters.joinToString(" &&\n        ", "", "\n") { param ->
            "${param.name!!.getShortName()}(it.${param.name!!.getShortName()})"
        })
        .endControlFlow()
        .endControlFlow()
        .build()

    private fun String.decap(): String = this.replaceFirstChar { it.lowercase(Locale.getDefault()) }

    // use the decapitalized class name as function name,
    // add "...Pattern" if it would otherwise cause a name clash
    private fun functionName(shortName: String) = shortName.decap()
        .let { decap ->
            if (decap == shortName) "${decap}Pattern" else decap
        }
}