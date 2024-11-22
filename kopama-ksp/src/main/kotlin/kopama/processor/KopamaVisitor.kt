@file:OptIn(KspExperimental::class)

package kopama.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.toTypeVariableName
import com.squareup.kotlinpoet.ksp.writeTo
import kopama.Kopama
import java.util.*

class KopamaVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : KSVisitorVoid() {

    private val patternClassName = ClassName("kopama", "Pattern")

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val annotation = classDeclaration.getAnnotationsByType(Kopama::class).first()

        val shortName = classDeclaration.simpleName.getShortName()
        val patternName = annotation.patternName.takeIf { it.isNotEmpty() } ?: shortName.decap()
        val patternFileName = annotation.patternFileName.takeIf { it.isNotEmpty() } ?: "${patternName}Pattern"

        logger.warn("found $shortName")

        if (Modifier.DATA !in classDeclaration.modifiers) {
            logger.error("Can't generate pattern, <$shortName> isn't a data class")
        }

        val parameters = classDeclaration.primaryConstructor!!.parameters
        val funSpec = patternFunction(patternName, parameters, classDeclaration)

        val fileSpec = FileSpec.builder(
            packageName = classDeclaration.packageName.asString(),
            fileName = patternFileName
        ).addFunction(funSpec).addImport("kopama", "any").build()

        fileSpec.writeTo(codeGenerator, false)
    }

    private fun patternFunction(
        patternName: String,
        parameters: List<KSValueParameter>,
        classDeclaration: KSClassDeclaration
    ) = FunSpec.builder(patternName)
        .addParameters(parameters.map { param ->
            ParameterSpec.builder(
                name = param.name!!.getShortName(),
                type = patternClassName.parameterizedBy(paramTypeName(param))
            ).defaultValue("any")
                .build()
        })
        .addTypeVariables(typeVariableNames(classDeclaration))
        .returns(patternClassName.parameterizedBy(returnType(classDeclaration).copy(nullable = true)))
        .beginControlFlow("return")
        .beginControlFlow("when(it)")
        .addCode("null -> false\n")
        .addCode("else -> %L", parameters.joinToString(" &&\n        ", "", "\n") { param ->
            "${param.name!!.getShortName()}(it.${param.name!!.getShortName()})"
        })
        .endControlFlow()
        .endControlFlow()
        .build()

    private fun typeVariableNames(classDeclaration: KSClassDeclaration) =
        classDeclaration.typeParameters.map { it.toTypeVariableName() }

    private fun returnType(classDeclaration: KSClassDeclaration) = when {
        classDeclaration.typeParameters.isNotEmpty() ->
            classDeclaration.toClassName().parameterizedBy(typeVariableNames(classDeclaration))

        else -> classDeclaration.toClassName()
    }

    private fun paramTypeName(param: KSValueParameter) : TypeName  {
        val ksType = param.type.resolve()
        return (if (ksType.arguments.isEmpty()) ksType.toClassName() else
            ksType.toClassName().parameterizedBy(ksType.arguments.map { it.toTypeName() }))
                .copy(nullable = ksType.nullability != Nullability.NOT_NULL)
    }

    private fun String.decap(): String = this.replaceFirstChar { it.lowercase(Locale.getDefault()) }
}