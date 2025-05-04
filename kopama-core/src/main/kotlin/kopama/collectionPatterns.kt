package kopama

/**
 * Matches if collection is empty.
 *
 * @return the resulting pattern.
 */
fun isEmpty(): Pattern<Collection<*>> =
    { it.isEmpty() }

/**
 * Matches if collection is not empty.
 *
 * @return the resulting pattern.
 */
fun isNotEmpty(): Pattern<Collection<*>> =
    { it.isNotEmpty() }

/**
 * Matches if collection has the given size.
 *
 * @param size the required size.
 * @return the resulting pattern.
 */
fun hasSize(size: Int): Pattern<Collection<*>> =
    { it.size == size }

/**
 * Matches if the size of the collection matches the given pattern.
 *
 * @param pattern the pattern defining a valid collection size.
 * @return the resulting pattern.
 */
fun hasSize(pattern: Pattern<Int>): Pattern<Collection<*>> =
    { pattern(it.size) }

/**
 * Matches if all elements of an iterable match the given pattern.
 *
 * @param P the element type of the iterable.
 * @param pattern the pattern defining valid elements.
 * @return the resulting pattern.
 */
fun <P> forAll(pattern: Pattern<P>): Pattern<Iterable<P>> =
    { it.all(pattern) }

/**
 * Matches if the iterable contains at least one element matching the given pattern.
 *
 * @param P the element type of the iterable.
 * @param pattern the pattern defining a required element.
 * @return the resulting pattern.
 */
fun <P> forAny(pattern: Pattern<P>): Pattern<Iterable<P>> =
    { it.any(pattern) }

/**
 * Matches if no element of the iterable matches the given pattern.
 *
 * @param P the element type of the iterable.
 * @param pattern the pattern defining invalid elements.
 * @return the resulting pattern.
 */
fun <P> forNone(pattern: Pattern<P>): Pattern<Iterable<P>> =
    { it.none(pattern) }

/**
 * Matches if the iterable contains the given element.
 *
 * @param P the element type of the iterable.
 * @param element the required element.
 * @return the resulting pattern.
 */
fun <P> contains(element: P): Pattern<Iterable<P>> =
    { element in it }

/**
 * Matches if the collection contains all given elements.
 *
 * @param P the element type of the collection.
 * @param elements the required elements.
 * @return the resulting pattern.
 */
fun <P> containsAll(vararg elements: P): Pattern<Collection<P>> =
    { it.containsAll(elements.toSet()) }

/**
 * Matches if the collection contains any of the given elements.
 *
 * @param P the element type of the collection.
 * @param elements the required elements.
 * @return the resulting pattern.
 */
fun <P> containsAny(vararg elements: P): Pattern<Collection<P>> =
    { collection -> elements.any { it in collection } }

/**
 * Matches if the collection contains none of the given elements.
 *
 * @param P the element type of the collection.
 * @param elements the invalid elements.
 * @return the resulting pattern.
 */
fun <P> containsNone(vararg elements: P): Pattern<Collection<P>> =
    { collection -> elements.none { it in collection } }

/**
 * Matches if the given pattern matches the list element at a certain index.
 *
 * Matching fails if the index is out of bounds.
 *
 * Example usage:
 *
 * ```
 * match(listOf(1,2,3)) {
 *    ...
 *    lt(5)[2] then { "the third entry is smaller than 5" }
 *    ...
 * }
 * ```
 *
 * @param P the element type of the list.
 * @param index the index of the element
 * @return the resulting pattern.
 */
operator fun <P> Pattern<P>.get(index: Int): Pattern<List<P>> =
    { 0 <= index && index < it.size && this@get(it[index]) }
