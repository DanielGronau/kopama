package kopama

fun arrayIsEmpty(): Pattern<Array<*>> =
    { it.isEmpty() }

fun arrayIsNotEmpty(): Pattern<Array<*>> =
    { it.isNotEmpty() }

fun arrayHasSize(size: Int): Pattern<Array<*>> =
    { it.size == size }

fun <P> arrayForAll(p: Pattern<P>): Pattern<Array<P>> =
    { it.all(p) }

fun <P> arrayForAny(p: Pattern<P>): Pattern<Array<P>> =
    { it.any(p) }

fun <P> arrayForNone(p: Pattern<P>): Pattern<Array<P>> =
    { it.none(p) }

fun <P> arrayContains(element: P): Pattern<Array<P>> =
    { element in it }

fun <P> arrayContainsAll(vararg elements: P): Pattern<Array<P>> =
    { it.toList().containsAll(elements.toSet()) }

fun <P> arrayContainsAny(vararg elements: P): Pattern<Array<P>> =
    { collection -> elements.any { it in collection } }


fun <P> arrayContainsNone(vararg elements: P): Pattern<Array<P>> =
    { collection -> elements.none { it in collection } }

fun <P> Pattern<P>.atIndex(index: Int): Pattern<List<P>> =
    { 0 <= index && index < it.size && this(it[index]) }
