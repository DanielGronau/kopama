package kopama

// Constant Patterns

fun <P> any(): Pattern<P> =
    { true }

fun <P> none(): Pattern<P> =
    { false }

// Operator Patterns

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

infix fun <P,Q> Pattern<Q>.on(transform: (P) -> Q): Pattern<P> =
    { this@on(transform(it)) }

// Comparing Patterns

fun <P> isNull(): Pattern<P> =
    { it == null }

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

// String Patterns

fun <P> hasToString(string: String): Pattern<P> =
    { it.toString() == string }

fun eqIgnoreCase(string: String): Pattern<String> =
    { it.equals(string, true) }

fun startsWith(string: String): Pattern<String> =
    { it.startsWith(string) }

fun endsWith(string: String): Pattern<String> =
    { it.endsWith(string) }

fun containsString(string: String): Pattern<String> =
    { it.contains(string) }

fun regex(regex: String): Pattern<String> =
    { it.matches(regex.toRegex()) }

// Collection Patterns

fun <P> isEmpty() : Pattern<Collection<P>> =
    { it.isEmpty() }

fun <P> isNotEmpty() : Pattern<Collection<P>> =
    { it.isNotEmpty() }

fun <P> hasSize(size: Int) : Pattern<Collection<P>> =
    { it.size == size }

fun <P> all(p: Pattern<P>) : Pattern<Iterable<P>> =
    { it.all(p) }

fun <P> any(p: Pattern<P>) : Pattern<Iterable<P>> =
    { it.any(p) }

fun <P> none(p: Pattern<P>) : Pattern<Iterable<P>> =
    { it.none(p) }

fun <P> contains(element: P) : Pattern<Iterable<P>> =
    { element in it}

fun <P> containsAll(vararg elements: P) : Pattern<Collection<P>> =
    { it.containsAll(elements.toSet()) }

fun <P> containsAny(vararg elements: P) : Pattern<Collection<P>> =
    { collection -> elements.any { it in collection } }

fun <P> containsNone(vararg elements: P) : Pattern<Collection<P>> =
    { collection -> elements.none { it in collection } }

operator fun <P> Pattern<P>.get(index: Int) : Pattern<List<P>> =
    { this@get(it[index]) }

// Map Patterns

fun <K,V> allKeys(p: Pattern<K>) : Pattern<Map<K, V>> =
    { it.keys.all(p) }

fun <K,V> anyKey(p: Pattern<K>) : Pattern<Map<K, V>> =
    { it.keys.any(p) }

fun <K,V> noKey(p: Pattern<K>) : Pattern<Map<K, V>> =
    { it.keys.none(p) }

fun <K,V> allValues(p: Pattern<V>) : Pattern<Map<K, V>> =
    { it.values.all(p) }

fun <K,V> anyValue(p: Pattern<V>) : Pattern<Map<K, V>> =
    { it.values.any(p) }

fun <K,V> noValue(p: Pattern<V>) : Pattern<Map<K, V>> =
    { it.values.none(p) }

