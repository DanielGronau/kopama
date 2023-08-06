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

fun <P> isEmpty(): Pattern<Collection<P>> =
    { it.isEmpty() }

fun <P> isNotEmpty(): Pattern<Collection<P>> =
    { it.isNotEmpty() }

fun <P> hasSize(size: Int): Pattern<Collection<P>> =
    { it.size == size }

fun <P> all(p: Pattern<P>): Pattern<Iterable<P>> =
    { it.all(p) }

fun <P> any(p: Pattern<P>): Pattern<Iterable<P>> =
    { it.any(p) }

fun <P> none(p: Pattern<P>): Pattern<Iterable<P>> =
    { it.none(p) }

fun <P> contains(element: P): Pattern<Iterable<P>> =
    { element in it }

fun <P> containsAll(vararg elements: P): Pattern<Collection<P>> =
    { it.containsAll(elements.toSet()) }

fun <P> containsAny(vararg elements: P): Pattern<Collection<P>> =
    { collection -> elements.any { it in collection } }


fun <P> containsNone(vararg elements: P): Pattern<Collection<P>> =
    { collection -> elements.none { it in collection } }

operator fun <P> Pattern<P>.get(index: Int): Pattern<List<P>> =
    { 0 <= index && index < it.size && this@get(it[index]) }

// Map Patterns

fun <K, V> keys(p: Pattern<Set<K>>): Pattern<Map<K, V>> =
    { p(it.keys) }


fun <K, V> values(p: Pattern<Collection<V>>): Pattern<Map<K, V>> =
    { p(it.values) }

fun <K, V> entries(p: Pattern<List<Pair<K, V>>>): Pattern<Map<K, V>> =
    { p(it.entries.map(Map.Entry<K, V>::toPair)) }

// Tuple Patterns

fun <A, B> pair(p: (A, B) -> Boolean): Pattern<Pair<A, B>> =
    { p(it.first, it.second) }

fun <A, B> pair(pa: Pattern<A>, pb: Pattern<B>): Pattern<Pair<A, B>> =
    { pa(it.first) && pb(it.second) }

fun <A, B> first(p: Pattern<A>): Pattern<Pair<A, B>> =
    { p(it.first) }

fun <A, B> second(p: Pattern<B>): Pattern<Pair<A, B>> =
    { p(it.second) }

fun <A, B, C> triple(p: (A, B, C) -> Boolean): Pattern<Triple<A, B, C>> =
    { p(it.first, it.second, it.third) }

fun <A, B, C> triple(pa: Pattern<A>, pb: Pattern<B>, pc: Pattern<C>): Pattern<Triple<A, B, C>> =
    { pa(it.first) && pb(it.second) && pc(it.third) }

fun <A, B, C> triple1(p: Pattern<A>): Pattern<Triple<A, B, C>> =
    { p(it.first) }

fun <A, B, C> triple2(p: Pattern<B>): Pattern<Triple<A, B, C>> =
    { p(it.second) }

fun <A, B, C> triple3(p: Pattern<C>): Pattern<Triple<A, B, C>> =
    { p(it.third) }
