package kopama

import java.util.*
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberFunctions

fun split(vararg patterns: Pattern): Pattern = Split(null, *patterns)

operator fun String.invoke(vararg patterns: Pattern): Pattern = Split(this, *patterns)

internal class Split(private val className: String?, vararg val patterns: Pattern) : Pattern {
    override fun test(obj: Any?) = when {
        obj == null -> false
        className != null && className != obj::class.simpleName && className != obj::class.qualifiedName -> false
        obj is Iterable<*> -> obj.zip(patterns) { elem, pattern -> if (pattern.test(elem)) 1 else 0 }
            .sum() == patterns.size
        else -> patterns.foldIndexed(true) { i, b, p -> b && p.testComponentN(obj, i + 1) }
    }
}

// index is 1-based
operator fun Pattern.get(index: Int) = object : Pattern {

    private fun testIndex(list: List<*>) =
        if (index < 0 || index >= list.size) false
        else this@get.test(list[index])

    private fun testChar(s: CharSequence) =
        if (index < 0 || index >= s.length) false
        else this@get.test(s[index])

    override fun test(obj: Any?) = when (obj) {
        null -> false
        is List<*> -> testIndex(obj)
        is Array<*> -> testIndex(obj.toList())
        is Sequence<*> -> testIndex(obj.toList())
        is Iterable<*> -> testIndex(obj.toList())
        is CharSequence -> testChar(obj)
        else -> this@get.testComponentN(obj, index)
    }
}

operator fun Pattern.get(propertyName: String) = object : Pattern {
    override fun test(obj: Any?): Boolean {
        if (obj == null)
            return false
        return obj::class.members
            .find {
                it.name == propertyName
                        || it.name == "get${capitalize(propertyName)}"
                        || it.name == "is${capitalize(propertyName)}"
            }
            ?.call(obj)
            ?.let { this@get.test(it) }
            ?: false
    }
}

private fun capitalize(s: String) =
    s.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

private fun Pattern.testComponentN(obj: Any?, index: Int) =
    if (index < 0 || obj == null) false
    else obj::class.memberFunctions.find { f ->
        f.name == "component$index" && f.parameters.size == 1 && f.parameters[0].kind == KParameter.Kind.INSTANCE
    }
        ?.call(obj)
        ?.let { this@testComponentN.test(it) }
        ?: false
