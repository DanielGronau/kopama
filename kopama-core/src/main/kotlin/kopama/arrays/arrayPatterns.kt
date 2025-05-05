package kopama.arrays

import kopama.Pattern

/**
 * Matches if array is empty.
 *
 * @return the resulting pattern.
 */
fun arrayIsEmpty(): Pattern<Array<*>> =
    { it.isEmpty() }

/**
 * Matches if array is not empty.
 *
 * @return the resulting pattern.
 */
fun arrayIsNotEmpty(): Pattern<Array<*>> =
    { it.isNotEmpty() }

/**
 * Matches if the array has the given size.
 *
 * @param size the required size.
 * @return the resulting pattern.
 */
fun arrayHasSize(size: Int): Pattern<Array<*>> =
    { it.size == size }

/**
 * Matches if array size matches the given pattern.
 *
 * @param pattern the pattern defining a valid array size.
 * @return the resulting pattern.
 */
fun arrayHasSize(pattern: Pattern<Int>): Pattern<Array<*>> =
    { pattern(it.size) }

/**
 * Matches if all array elements match the given pattern.
 *
 * @param P the array element type.
 * @param pattern the pattern defining valid elements.
 * @return the resulting pattern.
 */
fun <P> arrayForAll(pattern: Pattern<P>): Pattern<Array<P>> =
    { it.all(pattern) }

/**
 * Matches if the array contains at least one element matching the given pattern.
 *
 * @param P the array element type.
 * @param pattern the pattern defining a required element.
 * @return the resulting pattern.
 */
fun <P> arrayForAny(pattern: Pattern<P>): Pattern<Array<P>> =
    { it.any(pattern) }

/**
 * Matches if no element of the array matches the given pattern.
 *
 * @param P the array element type.
 * @param pattern the pattern defining invalid elements.
 * @return the resulting pattern.
 */
fun <P> arrayForNone(pattern: Pattern<P>): Pattern<Array<P>> =
    { it.none(pattern) }

/**
 * Matches if the array contains the given element.
 *
 * @param P the array element type.
 * @param element the required element.
 * @return the resulting pattern.
 */
fun <P> arrayContains(element: P): Pattern<Array<P>> =
    { element in it }

/**
 * Matches if the array contains all the given elements.
 *
 * @param P the array element type.
 * @param elements the required elements.
 * @return the resulting pattern.
 */
fun <P> arrayContainsAll(vararg elements: P): Pattern<Array<P>> =
    { it.toList().containsAll(elements.toSet()) }

/**
 * Matches if the array contains any of the given elements.
 *
 * @param P the array element type.
 * @param elements the required elements.
 * @return the resulting pattern.
 */
fun <P> arrayContainsAny(vararg elements: P): Pattern<Array<P>> =
    { collection -> elements.any { it in collection } }


/**
 * Matches if the array contains none of the given elements.
 *
 * @param P the array element type.
 * @param elements the invalid elements.
 * @return the resulting pattern.
 */
fun <P> arrayContainsNone(vararg elements: P): Pattern<Array<P>> =
    { collection -> elements.none { it in collection } }

/**
 * Matches if the given pattern matches the array element at a certain index.
 *
 * Matching fails if the index is out of bounds.
 *
 * Example usage:
 *
 * ```
 * match(arrayOf(1,2,3)) {
 *    ...
 *    lt(5)[2] then { "the third entry is smaller than 5" }
 *    ...
 * }
 * ```
 *
 * @param P the array element type.
 * @param index the index of the element.
 * @return the resulting pattern.
 */
infix fun <P> Pattern<P>.atIndex(index: Int): Pattern<Array<P>> =
    { 0 <= index && index < it.size && this(it[index]) }
