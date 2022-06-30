package kopama

import kotlin.reflect.full.memberFunctions

interface Pattern : Matching {
    fun test(obj: Any?): Boolean
}

val isNull by lazy {
    object : Pattern {
        override fun test(obj: Any?) = obj == null
    }
}

val any = object : Pattern {
    override fun test(obj: Any?) = true
}

operator fun Pattern.not() = object : Pattern {
    override fun test(obj: Any?) = !this@not.test(obj)
}

fun eq(value: Any?) = object : Pattern {
    override fun test(obj: Any?) = value == obj
}

fun oneOf(vararg values: Any?) = object : Pattern {
    override fun test(obj: Any?) = values.any { it == obj }
}

infix fun Pattern.and(that: Pattern) = object : Pattern {
    override fun test(obj: Any?) = this@and.test(obj) && that.test(obj)
}

infix fun Pattern.or(that: Pattern) = object : Pattern {
    override fun test(obj: Any?) = this@or.test(obj) || that.test(obj)
}

operator fun Pattern.rangeTo(that: Pattern) = when (this) {
    is Unapply -> Unapply(*this.patterns, that)
    else -> Unapply(this, that)
}

class Unapply internal constructor(vararg val patterns: Pattern) : Pattern {
    override fun test(obj: Any?) =
        if (obj == null) false
        else patterns.foldIndexed(true) { i, b, p ->
            b && p.test(obj::class.memberFunctions.find { f -> f.name == "component${i + 1}" }?.call(obj))
        }
}
