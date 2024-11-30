package kopama

operator fun <P> Pattern<P>.not(): Pattern<P> =
    { !this@not(it) }

infix fun <P> Pattern<P>.and(that: Pattern<P>): Pattern<P> =
    { this@and(it) && that(it) }

infix fun <P> Pattern<P>.or(that: Pattern<P>): Pattern<P> =
    { this@or(it) || that(it) }

infix fun <P> Pattern<P>.xor(that: Pattern<P>): Pattern<P> =
    { this@xor(it) xor that(it) }

fun <P> allOf(vararg patterns: Pattern<P>): Pattern<P> =
    { obj -> patterns.all { it(obj) } }

fun <P> anyOf(vararg patterns: Pattern<P>): Pattern<P> =
    { obj -> patterns.any { it(obj) } }

fun <P> noneOf(vararg patterns: Pattern<P>): Pattern<P> =
    { obj -> patterns.none { it(obj) } }

infix fun <P, Q> Pattern<Q>.on(transform: (P) -> Q): Pattern<P> =
    { this@on(transform(it)) }

infix fun <P> Pattern<P>.thenRequire(p: Pattern<P>): Pattern<P> = {
    when {
        this@thenRequire(it) -> p(it)
        else -> true
    }
}

fun <P> ifThenElse(cond: Pattern<P>, whenTrue: Pattern<P>, whenFalse: Pattern<P>): Pattern<P> =
    { if (cond(it)) whenTrue(it) else whenFalse(it) }
