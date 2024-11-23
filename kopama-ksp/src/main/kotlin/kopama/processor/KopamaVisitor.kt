@file:OptIn(KspExperimental::class)

package kopama.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isOpen
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
        val fileName = annotation.fileName.takeIf { it.isNotEmpty() } ?: "${patternName}Pattern"

        logger.warn("found $shortName")

        if (classDeclaration.isOpen()) {
            logger.error("Can't generate pattern, <$shortName> isn't a final class")
        }

        val parameters = when {
            annotation.arguments.isEmpty() -> classDeclaration.primaryConstructor!!.parameters
                .filter { it.isVal || it.isVar }
                .map { param ->
                    ClassProperty(name = param.name!!.getShortName(), type = param.type.resolve())
                }

            else -> findParameters(annotation.arguments.asList(), classDeclaration)
        }
        val funSpec = patternFunction(patternName, parameters, classDeclaration)

        val fileSpec = FileSpec.builder(
            packageName = classDeclaration.packageName.asString(),
            fileName = fileName
        ).addFunction(funSpec).addImport("kopama", "any").build()

        fileSpec.writeTo(codeGenerator, false)
    }

    private fun findParameters(
        parameterNames: List<String>,
        classDeclaration: KSClassDeclaration
    ): List<ClassProperty> = parameterNames.map { paramTypeName ->
        classDeclaration.primaryConstructor?.parameters?.firstOrNull {
            (it.isVal || it.isVar) && it.name?.getShortName() == paramTypeName
        }?.let { param ->
            ClassProperty(name = param.name!!.getShortName(), type = param.type.resolve())
        } ?: classDeclaration.getAllProperties().firstOrNull {
            it.simpleName.getShortName() == paramTypeName
        }?.let { declaration ->
            ClassProperty(name = declaration.qualifiedName!!.getShortName(), type = declaration.type.resolve())
        } ?: classDeclaration.getAllFunctions().firstOrNull {
            it.parameters.isEmpty() && it.simpleName.getShortName() == paramTypeName
        }?.let { function ->
            ClassProperty(
                name = function.qualifiedName!!.getShortName(),
                type = function.returnType!!.resolve(),
                isFun = true
            )
        }
        ?: error("Can't generate pattern, couldn't find parameter <$paramTypeName>").also {
            logger.error("Can't generate pattern, couldn't find parameter <$paramTypeName>")
        }
    }

    private fun patternFunction(
        patternName: String,
        parameters: List<ClassProperty>,
        classDeclaration: KSClassDeclaration
    ) = FunSpec.builder(patternName)
        .addParameters(parameters.map { prop ->
            ParameterSpec.builder(
                name = prop.name,
                type = patternClassName.parameterizedBy(paramTypeName(prop.type))
            ).defaultValue("any")
                .build()
        })
        .addTypeVariables(classDeclaration.typeVariableNames())
        .returns(patternClassName.parameterizedBy(classDeclaration.returnType().copy(nullable = true)))
        .beginControlFlow("return")
        .beginControlFlow("when(it)")
        .addCode("null -> false\n")
        .addCode("else -> %L", parameters.joinToString(" &&\n        ", "", "\n") { param ->
            "${param.name}(it.${param.name}${if (param.isFun) "()" else ""})"
        })
        .endControlFlow()
        .endControlFlow()
        .build()

    private fun KSClassDeclaration.typeVariableNames() =
        typeParameters.map { it.toTypeVariableName() }

    private fun KSClassDeclaration.returnType() = when {
        typeParameters.isNotEmpty() ->
            toClassName().parameterizedBy(typeVariableNames())

        else -> toClassName()
    }

    private fun paramTypeName(ksType: KSType): TypeName {
        return (if (ksType.arguments.isEmpty()) ksType.toClassName() else
            ksType.toClassName().parameterizedBy(ksType.arguments.map { it.toTypeName() }))
            .copy(nullable = ksType.nullability != Nullability.NOT_NULL)
    }

    private fun String.decap(): String = this.replaceFirstChar { it.lowercase(Locale.getDefault()) }

    private data class ClassProperty(val name: String, val type: KSType, val isFun: Boolean = false)
}