package kopama

operator fun Pattern.not() = object : Pattern {
    override fun test(obj: Any?) = !this@not.test(obj)
}

infix fun Pattern.and(that: Pattern) = object : Pattern {
    override fun test(obj: Any?) = this@and.test(obj) && that.test(obj)
}

infix fun Pattern.or(that: Pattern) = object : Pattern {
    override fun test(obj: Any?) = this@or.test(obj) || that.test(obj)
}

infix fun Pattern.xor(that: Pattern) = object : Pattern {
    override fun test(obj: Any?) = this@xor.test(obj) xor that.test(obj)
}

fun allOf(vararg patterns: Pattern) = object : Pattern {
    override fun test(obj: Any?) = patterns.all { it.test(obj) }
}

fun anyOf(vararg patterns: Pattern) = object : Pattern {
    override fun test(obj: Any?) = patterns.any { it.test(obj) }
}