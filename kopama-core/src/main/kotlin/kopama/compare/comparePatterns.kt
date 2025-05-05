package kopama.compare

import kopama.Pattern

/**
 * The pattern that always matches.
 */
val any: Pattern<Any?> =
    { true }

/**
 * The pattern that never matches.
 */
val none: Pattern<Any?> =
    { false }

/**
 * Alternative version of `any`.
 *
 * Can be used to explicitly specify a type in order to avoid ambiguity, e.g. when matching a generic class.
 *
 * @param P the type of the target object.
 * @return the resulting pattern.
 */
fun <P> any(): Pattern<P> = { true }

/**
 * Alternative version of `none`.
 *
 * Can be used to explicitly specify a type in order to avoid ambiguity, e.g. when matching a generic class.
 *
 * @param P the type of the target object.
 * @return the resulting pattern.
 */
fun <P> none(): Pattern<P> = { false }

/**
 * Matches if the value is null.
 * @return the resulting pattern.
 */
val isNull: Pattern<Any?> =
    { it == null }

/**
 * Matches if the value is not null.
 * @return the resulting pattern.
 */
val isNotNull: Pattern<Any?> =
    { it != null }

/**
 * Matches if the value is null or the given pattern matches.
 *
 * @param P the pattern target type.
 * @param pattern the pattern required if the value is not null.
 * @return the resulting pattern.
 */
fun <P> isNullOr(pattern: Pattern<P>): Pattern<P?> =
    { it == null || pattern(it) }

/**
 * Matches if the value is not null and the given pattern matches.
 *
 * @param P the pattern target type.
 * @param pattern the pattern required if the value is not null.
 * @return the resulting pattern.
 */
fun <P> isNotNullAnd(pattern: Pattern<P>): Pattern<P?> =
    { it != null && pattern(it) }

/**
 * Matches if the value is equal to the given value.
 *
 * Note that you can use the unary plus instead (if not already defined, as for numbers) inside a match block,
 * e.g. `eq("foo")` can be written as `+"foo"`.
 *
 * @param P the pattern target type.
 * @param value the value to compare against.
 * @return the resulting pattern.
 */
fun <P> eq(value: P): Pattern<P> =
    { it == value }

/**
 * Matches if the value is the same instance as the given value.
 *
 * @param P the pattern target type.
 * @param value the value to compare against.
 * @return the resulting pattern.
 */
fun <P> isSame(value: P): Pattern<P> =
    { it === value }

/**
 * Matches if the value is equal to one of the given values.
 *
 * @param P the pattern target type.
 * @param values the values to compare against.
 * @return the resulting pattern.
 */
fun <P> oneOf(vararg values: P): Pattern<P> =
    { it in values }

/**
 * Matches if the value is of the given type.
 *
 * @param Q the required type.
 * @return the resulting pattern.
 */
inline fun <reified Q> isA(): Pattern<Any> =
    { Q::class.isInstance(it) }

/**
 * Matches if the value is greater than the given value.
 *
 * @param C the comparable pattern target type.
 * @param value the value to compare against.
 * @return the resulting pattern.
 */
inline fun <reified C : Comparable<C>> gt(value: C): Pattern<C> =
    { it > value }

/**
 * Matches if the value is greater than or equal to the given value.
 *
 * @param C the comparable pattern target type.
 * @param value the value to compare against.
 * @return the resulting pattern.
 */
inline fun <reified C : Comparable<C>> ge(value: C): Pattern<C> =
    { it >= value }

/**
 * Matches if the value is less than the given value.
 *
 * @param C the comparable pattern target type.
 * @param value the value to compare against.
 * @return the resulting pattern.
 */
inline fun <reified C : Comparable<C>> lt(value: C): Pattern<C> =
    { it < value }

/**
 * Matches if the value is less than or equal to the given value.
 *
 * @param C the comparable pattern target type.
 * @param value the value to compare against.
 * @return the resulting pattern.
 */
inline fun <reified C : Comparable<C>> le(value: C): Pattern<C> =
    { it <= value }

/**
 * Matches if the value is between the given values or equal to them.
 *
 * @param C the comparable pattern target type.
 * @param lower the lower value (inclusive).
 * @param upper the upper value (inclusive).
 * @return the resulting pattern.
 */
inline fun <reified C : Comparable<C>> between(lower: C, upper: C): Pattern<C> =
    { it in lower..upper }

/**
 * Matches if the value is in the given closed range.
 *
 * @param C the comparable pattern target type.
 * @param range the range the value should fall in.
 * @return the resulting pattern.
 */
inline fun <reified C : Comparable<C>> inRange(range: ClosedRange<C>): Pattern<C> =
    { it in range }
