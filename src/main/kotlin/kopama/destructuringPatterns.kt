package kopama

import kotlin.reflect.full.memberFunctions

fun data(vararg patterns: Pattern) = Destruct(null, *patterns)

operator fun String.invoke(vararg patterns: Pattern) = Destruct(this, *patterns)

class Destruct internal constructor(private val className: String?, vararg val patterns: Pattern) : Pattern {
    override fun test(obj: Any?) = when {
        obj == null -> false
        className != null && className != obj::class.simpleName && className != obj::class.qualifiedName -> false
        obj is Iterable<*> -> obj.zip(patterns) { elem, pattern -> if (pattern.test(elem)) 1 else 0}
            .sum() == patterns.size
        else -> patterns.foldIndexed(true) { i, b, p ->
            b && p.test(obj::class.memberFunctions.find { f -> f.name == "component${i + 1}" }?.call(obj))
        }
    }
}
