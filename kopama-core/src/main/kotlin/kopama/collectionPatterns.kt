package kopama

fun isEmpty(): Pattern<Collection<*>> =
    { it.isEmpty() }

fun isNotEmpty(): Pattern<Collection<*>> =
    { it.isNotEmpty() }

fun hasSize(size: Int): Pattern<Collection<*>> =
    { it.size == size }

fun <P> forAll(p: Pattern<P>): Pattern<Iterable<P>> =
    { it.all(p) }

fun <P> forAny(p: Pattern<P>): Pattern<Iterable<P>> =
    { it.any(p) }

fun <P> forNone(p: Pattern<P>): Pattern<Iterable<P>> =
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
