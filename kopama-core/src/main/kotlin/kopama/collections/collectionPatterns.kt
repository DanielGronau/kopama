package kopama.collections

import kopama.Pattern

/**
 * Matches if collection is empty.
 *
 * Example:
 *
 *     val s = match(listOf<Int>()) {
 *         isEmpty() then { "empty list" } // matches
 *         otherwise { "non-empty list" }
 *     }
 *
 * @return the resulting pattern.
 */
fun isEmpty(): Pattern<Collection<*>> =
    { it.isEmpty() }

/**
 * Matches if collection is not empty.
 *
 * Example:
 *
 *     val s = match(listOf(2, 3, 7)) {
 *         isNotEmpty() then { "non-empty list" } // matches
 *         otherwise { "empty list" }
 *     }
 *
 * @return the resulting pattern.
 */
fun isNotEmpty(): Pattern<Collection<*>> =
    { it.isNotEmpty() }

/**
 * Matches if collection has the given size.
 *
 * Example:
 *
 *     val s = match(listOf(2, 4, 7)) {
 *         hasSize(1) then { "singleton list" }
 *         hasSize(3) then { "list of size 3" } // matches
 *         otherwise { "other list" }
 *     }
 *
 * @param size the required size.
 * @return the resulting pattern.
 */
fun hasSize(size: Int): Pattern<Collection<*>> =
    { it.size == size }

/**
 * Matches if the size of the collection matches the given pattern.
 *
 * Example:
 *
 *     val s = match(listOf(2, 4, 7, 14, 17, 19)) {
 *         hasSize(ge(10)) then { "long list" }
 *         hasSize(ge(5)) then { "medium list" } // matches
 *         otherwise { "short list" }
 *     }
 *
 * @param pattern the pattern defining a valid collection size.
 * @return the resulting pattern.
 */
fun hasSize(pattern: Pattern<Int>): Pattern<Collection<*>> =
    { pattern(it.size) }

/**
 * Matches if all elements of an iterable match the given pattern.
 *
 * Example:
 *
 *     val s = match(listOf(2, 4, 14, 18)) {
 *         forAll{ it % 2 == 1 } then { "odd number list" }
 *         forAll{ it % 2 == 0 } then { "even number list" } // matches
 *         otherwise { "mixed list" }
 *     }
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
 * Example:
 *
 *     val s = match(listOf(1, 4, 13, 17)) {
 *         forAny{ it % 2 == 0 } then { "list with an even number" } // matches
 *         otherwise { "only odd numbers" }
 *     }
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
 * Example:
 *
 *     val s = match(listOf(2, 4, 14, 18)) {
 *         forNone{ it % 2 == 0 } then { "odd number list" }
 *         forNone{ it % 2 == 1 } then { "even number list" } // matches
 *         otherwise { "mixed list" }
 *     }
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
 * Example:
 *
 *     val s = match(listOf(2, 4, 14, 18)) {
 *         contains(15) then { "list with number 15" }
 *         contains(18) then { "list with number 18" } // matches
 *         otherwise { "other list" }
 *     }
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
 * Example:
 *
 *     val s = match(listOf(2, 4, 14, 18)) {
 *         containsAll(2, 4, 7) then { "list with 2, 4, 7" }
 *         containsAll(18, 4) then { "list with 18 and 4" } // matches
 *         otherwise { "other list" }
 *     }
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
 * Example:
 *
 *     val s = match(listOf(2, 4, 14, 18)) {
 *         containsAny(3, 4, 7) then { "list contains 3, 4 or 7" } // matches
 *         otherwise { "other list" }
 *     }
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
 * Example:
 *
 *     val s = match(listOf(2, 4, 14, 18)) {
 *         containsNone(3, 4, 7) then { "list doesn't contain 3, 4 or 7" }
 *         containsNone(5, 6, 7) then { "list doesn't contain 5, 6 or 7" } // matches
 *         otherwise { "other list" }
 *     }
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
 * Example:
 *
 *     val s = match(listOf(1, 2, 3)) {
 *         lt(5)[2] then { "the third entry is smaller than 5" } // matches
 *         otherwise { "other list" }
 *     }
 *
 * @param P the element type of the list.
 * @param index the index of the element
 * @return the resulting pattern.
 */
operator fun <P> Pattern<P>.get(index: Int): Pattern<List<P>> =
    { 0 <= index && index < it.size && this@get(it[index]) }
