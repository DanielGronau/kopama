package kopama

operator fun Pattern.not() = Pattern { obj -> !this@not.test(obj) }

infix fun Pattern.and(that: Pattern) = Pattern { obj -> this@and.test(obj) && that.test(obj) }

infix fun Pattern.or(that: Pattern) = Pattern { obj -> this@or.test(obj) || that.test(obj) }

infix fun Pattern.xor(that: Pattern) = Pattern { obj -> this@xor.test(obj) xor that.test(obj) }

fun allOf(vararg patterns: Pattern) = Pattern { obj -> patterns.all { it.test(obj) } }

fun anyOf(vararg patterns: Pattern) = Pattern { obj -> patterns.any { it.test(obj) } }