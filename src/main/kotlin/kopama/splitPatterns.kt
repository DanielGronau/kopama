package kopama

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

operator fun Pattern.get(key: Any) = object : Pattern {

    private fun findMember(obj: Any?, path: List<String>): List<Any?>? =
        when {
            path.isEmpty() -> listOf(obj)
            obj == null -> null
            else -> findMember(
                obj::class.members.find { it.name == path.first() }
                    ?.call(obj),
                path.drop(1)
            )
        }

    private fun testIndex(list: List<*>, index: Int) =
        if (index < 0 || index >= list.size) false
        else this@get.test(list[index])

    private fun testChar(s: CharSequence, index: Int) =
        if (index < 0 || index >= s.length) false
        else this@get.test(s[index])

    override fun test(obj: Any?) = when {
        obj == null -> false
        obj is Map<*, *> -> this@get.test(obj[key])
        key is String -> findMember(obj, key.split('.'))
            ?.let { this@get.test(it[0]) }
            ?: false
        key is Int && obj is List<*> -> testIndex(obj, key)
        key is Int && obj is Array<*> -> testIndex(obj.toList(), key)
        key is Int && obj is Sequence<*> -> testIndex(obj.toList(), key)
        key is Int && obj is Iterable<*> -> testIndex(obj.toList(), key)
        key is Int && obj is CharSequence -> testChar(obj, key)
        key is Int -> this@get.testComponentN(obj, key)
        else -> false
    }
}

private fun Pattern.testComponentN(obj: Any?, index: Int) =
    if (index < 0 || obj == null) false
    else obj::class.memberFunctions.find { f ->
        f.name == "component$index" && f.parameters.size == 1 && f.parameters[0].kind == KParameter.Kind.INSTANCE
    }
        ?.call(obj)
        ?.let { this@testComponentN.test(it) }
        ?: false
