package kopama

import kotlin.reflect.full.memberFunctions

val isNull by lazy {
    object : Pattern {
        override fun test(obj: Any?) = obj == null
    }
}

val any = object : Pattern {
    override fun test(obj: Any?) = true
}

fun eq(value: Any?) = object : Pattern {
    override fun test(obj: Any?) = value == obj
}

fun oneOf(vararg values: Any?) = object : Pattern {
    override fun test(obj: Any?) = values.any { it == obj }
}

/* pattern operators */

operator fun Pattern.not() = object : Pattern {
    override fun test(obj: Any?) = !this@not.test(obj)
}

infix fun Pattern.and(that: Pattern) = object : Pattern {
    override fun test(obj: Any?) = this@and.test(obj) && that.test(obj)
}

infix fun Pattern.or(that: Pattern) = object : Pattern {
    override fun test(obj: Any?) = this@or.test(obj) || that.test(obj)
}

/* destructuring patterns */

operator fun Pattern.rangeTo(that: Pattern) = when (this) {
    is Unapply -> Unapply(null, *this.patterns, that)
    else -> Unapply(null, this, that)
}

operator fun String.invoke(vararg patterns: Pattern) = Unapply(this, *patterns)

class Unapply internal constructor(private val className: String?, vararg val patterns: Pattern) : Pattern {
    override fun test(obj: Any?) = when {
        obj == null -> false
        className != null && className != obj::class.simpleName && className != obj::class.qualifiedName -> false
        else -> patterns.foldIndexed(true) { i, b, p ->
            b && p.test(obj::class.memberFunctions.find { f -> f.name == "component${i + 1}" }?.call(obj))
        }
    }
}
