package kopama

import kotlin.reflect.KParameter
import kotlin.reflect.full.memberFunctions

fun data(vararg patterns: Pattern) = Destruct(null, *patterns)

operator fun String.invoke(vararg patterns: Pattern) = Destruct(this, *patterns)

class Destruct internal constructor(private val className: String?, vararg val patterns: Pattern) : Pattern {
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

    private fun eval(list: List<*>) =
        if (index < 0 || index >= list.size) false
        else this@get.test(list[index])

    override fun test(obj: Any?) = when (obj) {
        null -> false
        is List<*> -> eval(obj)
        is Array<*> -> eval(obj.toList())
        is Sequence<*> -> eval(obj.toList())
        is Iterable<*> -> eval(obj.toList())
        else -> this@get.testComponentN(obj, index)
    }
}

private fun Pattern.testComponentN(obj: Any?, index: Int) =
    if (index < 0 || obj == null) false
    else obj::class.memberFunctions.find { f ->
        f.name == "component$index" && f.parameters.size == 1 && f.parameters[0].kind == KParameter.Kind.INSTANCE
    }?.call(obj)?.let { this@testComponentN.test(it) } ?: false
