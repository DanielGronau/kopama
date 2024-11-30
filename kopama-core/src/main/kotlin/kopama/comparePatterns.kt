package kopama

val isNull: Pattern<Any?> =
    { it == null }

val isNotNull: Pattern<Any?> =
    { it != null }

fun <P> isNullOr(pattern: Pattern<P>): Pattern<P?> =
    { it == null || pattern(it) }

fun <P> isNotNullAnd(pattern: Pattern<P>): Pattern<P?> =
    { it != null && pattern(it) }

fun <P> eq(value: P): Pattern<P> =
    { it == value }

fun <P> isSame(value: P): Pattern<P> =
    { it === value }

fun <P> oneOf(vararg values: P): Pattern<P> =
    { it in values }

inline fun <reified Q> isA(): Pattern<Any> =
    { Q::class.isInstance(it) }

inline fun <reified C : Comparable<C>> gt(value: C): Pattern<C> =
    { it > value }

inline fun <reified C : Comparable<C>> ge(value: C): Pattern<C> =
    { it >= value }

inline fun <reified C : Comparable<C>> lt(value: C): Pattern<C> =
    { it < value }

inline fun <reified C : Comparable<C>> le(value: C): Pattern<C> =
    { it <= value }

inline fun <reified C : Comparable<C>> between(lower: C, upper: C): Pattern<C> =
    { it in lower..upper }

inline fun <reified C : Comparable<C>> inRange(range: ClosedRange<C>): Pattern<C> =
    { it in range }
