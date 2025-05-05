package kopama.operators

import kopama.Pattern

/**
 * Matches if the given pattern doesn't match.
 *
 * @param P the pattern target type.
 * @return the resulting pattern.
 */
operator fun <P> Pattern<P>.not(): Pattern<P> =
    { !this@not(it) }

/**
 * Matches if both given patterns match.
 *
 * Note that the second pattern is not checked if the first is false.
 *
 * @param P the pattern target type.
 * @receiver the first pattern.
 * @param that the second pattern.
 * @return the resulting pattern.
 */
infix fun <P> Pattern<P>.and(that: Pattern<P>): Pattern<P> =
    { this@and(it) && that(it) }

/**
 * Matches if at least one of the pattern matches.
 *
 * Note that the second pattern is not checked if the first is true.
 *
 * @param P the pattern target type.
 * @receiver the first pattern.
 * @param that the second pattern.
 * @return the resulting pattern.
 */
infix fun <P> Pattern<P>.or(that: Pattern<P>): Pattern<P> =
    { this@or(it) || that(it) }

/**
 * Matches if exactly one of the pattern matches.
 *
 * @param P the pattern target type.
 * @receiver the first pattern.
 * @param that the second pattern.
 * @return the resulting pattern.
 */
infix fun <P> Pattern<P>.xor(that: Pattern<P>): Pattern<P> =
    { this@xor(it) xor that(it) }

/**
 * Matches if all given patterns match (generalized `and`).
 *
 * @param P the pattern target type.
 * @param patterns the required patterns.
 * @return the resulting pattern.
 */
fun <P> allOf(vararg patterns: Pattern<P>): Pattern<P> =
    { obj -> patterns.all { it(obj) } }

/**
 * Matches if any of the given patterns match (generalized `or`).
 *
 * @param P the pattern target type.
 * @param patterns the required patterns.
 * @return the resulting pattern.
 */
fun <P> anyOf(vararg patterns: Pattern<P>): Pattern<P> =
    { obj -> patterns.any { it(obj) } }

/**
 * Matches if none of the given patterns matches.
 *
 * @param P the pattern target type.
 * @param patterns the invalid patterns.
 * @return the resulting pattern.
 */
fun <P> noneOf(vararg patterns: Pattern<P>): Pattern<P> =
    { obj -> patterns.none { it(obj) } }

/**
 * Matches when the transformed value matches the given pattern.
 *
 * @param P the pattern target type.
 * @param Q the transformed type.
 * @param transform the mapping function.
 * @receiver the required pattern for the transformed type.
 * @return the resulting pattern.
 */
infix fun <P, Q> Pattern<Q>.on(transform: (P) -> Q): Pattern<P> =
    { this@on(transform(it)) }

/**
 * Matches if either the initial pattern fails, or the second pattern matches.
 *
 * E.g. to check that a field for an email address contains a '@' if it is not empty, you could write:
 * `ifNotEmpty() thenRequire contains("@")`
 *
 * @param P the pattern target type.
 * @param pattern the second pattern.
 * @receiver the initial pattern.
 * @return the resulting pattern.
 */
infix fun <P> Pattern<P>.thenRequire(pattern: Pattern<P>): Pattern<P> = {
    when {
        this@thenRequire(it) -> pattern(it)
        else -> true
    }
}

/**
 * Matches when the first and second pattern match, or when the first pattern fails and the third pattern matches.
 *
 * @param P the pattern target type.
 * @param cond the condition pattern.
 * @param whenTrue the required pattern if the condition is true.
 * @param whenFalse the required pattern if the condition is false.
 * @return the resulting pattern.
 */
fun <P> ifThenElse(cond: Pattern<P>, whenTrue: Pattern<P>, whenFalse: Pattern<P>): Pattern<P> =
    { if (cond(it)) whenTrue(it) else whenFalse(it) }
