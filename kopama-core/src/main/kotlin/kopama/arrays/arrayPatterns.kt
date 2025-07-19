package kopama.arrays

import kopama.Pattern

/**
 * Matches if the array is empty.
 *
 * Example:
 *
 *     val s = match(arrayOf<Int>()) {
 *         arrayIsEmpty() then { "empty array" } // matches
 *         otherwise { "array is not empty" }
 *     }
 *
 * @return the resulting pattern.
 */
fun arrayIsEmpty(): Pattern<Array<*>> =
    { it.isEmpty() }

/**
 * Matches if the array is not empty.
 *
 * Example:
 *
 *     val s = match(arrayOf(2, 3, 5)) {
 *         arrayIsNotEmpty() then { "array is not empty" } // matches
 *         otherwise { "array is empty" }
 *     }
 *
 * @return the resulting pattern.
 */
fun arrayIsNotEmpty(): Pattern<Array<*>> =
    { it.isNotEmpty() }

/**
 * Matches if the array has the given size.
 *
 * Example:
 *
 *     val s = match(arrayOf(2, 3, 5)) {
 *         arrayHasSize(1) then { "single entry" }
 *         arrayHasSize(3) then { "three entries" } // matches
 *         otherwise { "unknown array size" }
 *     }
 *
 * @param size the required size.
 * @return the resulting pattern.
 */
fun arrayHasSize(size: Int): Pattern<Array<*>> =
    { it.size == size }

/**
 * Matches if the array size matches the given pattern.
 *
 * Example:
 *
 *     val s = match(arrayOf(1, 2, 3, 5, 8, 13)) {
 *         arrayHasSize(ge(10)) then { "big array" }
 *         arrayHasSize(ge(5)) then { "medium array" } // matches
 *         otherwise { "small array" }
 *     }
 *
 * @param pattern the pattern defining a valid array size.
 * @return the resulting pattern.
 */
fun arrayHasSize(pattern: Pattern<Int>): Pattern<Array<*>> =
    { pattern(it.size) }

/**
 * Matches if all array elements match the given pattern.
 *
 * Example:
 *
 *     val s = match(arrayOf(5, 13, 227)) {
 *         arrayForAll{ it % 2 == 0 } then { "even number array" }
 *         arrayForAll{ it % 2 == 1 } then { "odd number array" } // matches
 *         otherwise { "mixed number array" }
 *     }
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
 * Example:
 *
 *     val s = match(arrayOf(6, 13, 222)) {
 *         arrayForAny{ it % 2 == 1 } then { "array with odd number" } // matches
 *         otherwise { "array without odd number" }
 *     }
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
 * Example:
 *
 *     val s = match(arrayOf(6, 12, 222)) {
 *         arrayForNone{ it % 2 == 1 } then { "array without odd number" } // matches
 *         otherwise { "array with odd number" }
 *     }
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
 * Example:
 *
 *     val s = match(arrayOf(6, 12, 42)) {
 *         arrayContains(42) then { "array knows answer" } // matches
 *         otherwise { "array doesn't know answer" }
 *     }
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
 * Example:
 *
 *     val s = match(arrayOf(6, 11, 47, 66)) {
 *         arrayContainsAll(47, 11) then { "scented array" } // matches
 *         otherwise { "normal array" }
 *     }
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
 * Example:
 *
 *     val s = match(arrayOf(6, 8, 11, 47, 66)) {
 *         arrayContainsAll(1, 2, 3, 5, 8, 13) then { "array with Fibonacci number" } // matches
 *         otherwise { "normal array" }
 *     }
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
 * Example:
 *
 *     val s = match(arrayOf(6, 9, 11, 47, 66)) {
 *         arrayContainsNone(1, 2, 3, 5, 8, 13) then { "array without Fibonacci number" } // matches
 *         otherwise { "array with Fibonacci number" }
 *     }
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
 *     val s = match(arrayOf(1, 2, 3, 6)) {
 *         lt(5)[2] then { "the third entry is smaller than 5" } // matches
 *         otherwise {  "the third entry is not smaller than 5" }
 *     }
 *
 * @param P the array element type.
 * @param index the index of the element.
 * @return the resulting pattern.
 */
infix fun <P> Pattern<P>.atIndex(index: Int): Pattern<Array<P>> =
    { index in 0 ..< it.size && this(it[index]) }
