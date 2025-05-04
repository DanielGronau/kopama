package kopama

/**
 * Matches for a pair when the given condition matches.
 *
 * @param A the type of the first element.
 * @param B the type of the second element.
 * @param condition the required condition.
 * @return the resulting pattern.
 */
fun <A, B> pair(condition: (A, B) -> Boolean): Pattern<Pair<A, B>> =
    { condition(it.first, it.second) }

/**
 * Matches for a pair when the given patterns for the first and second element match.
 *
 * @param A the type of the first element.
 * @param B the type of the second element.
 * @param first the required pattern for the first element.
 * @param second the required pattern for the second element.
 * @return the resulting pattern.
 */
fun <A, B> pair(first: Pattern<A> = any, second: Pattern<B> = any): Pattern<Pair<A, B>> =
    { first(it.first) && second(it.second) }

/**
 * Alternative version of `pair()` to avoid the need for explicit type parameters.
 *
 * Can only be used as pattern of a clause, not as a pattern nested inside other patterns.
 *
 * @param A the type of the first element.
 * @param B the type of the second element.
 * @param first the required pattern for the first element.
 * @param second the required pattern for the second element.
 * @return the resulting pattern.
 */
fun <A, B> Matcher<Pair<A, B>, *>.pair_(first: Pattern<A> = any, second: Pattern<B> = any): Pattern<Pair<A, B>> =
    { first(it.first) && second(it.second) }

/**
 * Matches for a pair when the given pattern for the first element matches.
 *
 * @param A the type of the first element.
 * @param B the type of the second element.
 * @param pattern the required pattern for the first element.
 * @return the resulting pattern.
 */
fun <A, B> first(pattern: Pattern<A>): Pattern<Pair<A, B>> =
    { pattern(it.first) }

/**
 * Matches for a pair when the given pattern for the second element matches.
 *
 * @param A the type of the first element.
 * @param B the type of the second element.
 * @param pattern the required pattern for the second element.
 * @return the resulting pattern.
 */
fun <A, B> second(pattern: Pattern<B>): Pattern<Pair<A, B>> =
    { pattern(it.second) }

/**
 * Triple
 *
 * @param A the type of the first element.
 * @param B the type of the second element.
 * @param C the type of the third element.
 * @param p
 * @receiver
 * @return the resulting pattern.
 */
fun <A, B, C> triple(p: (A, B, C) -> Boolean): Pattern<Triple<A, B, C>> =
    { p(it.first, it.second, it.third) }

/**
 * Matches for a triple when the given patterns for the first, second and third element match.
 *
 * @param A the type of the first element.
 * @param B the type of the second element.
 * @param C the type of the third element.
 * @param p1 the required pattern for the first element.
 * @param p2 the required pattern for the second element.
 * @param p3 the required pattern for the third element.
 * @return the resulting pattern.
 */
fun <A, B, C> triple(
    p1: Pattern<A> = any,
    p2: Pattern<B> = any,
    p3: Pattern<C> = any
): Pattern<Triple<A, B, C>> =
    { p1(it.first) && p2(it.second) && p3(it.third) }

/**
 * Alternative version of `triple()` to avoid the need for explicit type parameters.
 *
 * Can only be used as pattern of a clause, not as a pattern nested inside other patterns.
 *
 * @param A the type of the first element.
 * @param B the type of the second element.
 * @param C the type of the third element.
 * @param p1 the required pattern for the first element.
 * @param p2 the required pattern for the second element.
 * @param p3 the required pattern for the third element.
 * @return the resulting pattern.
 */// top-level version of triple, which avoids the need for explicit type parameters
fun <A, B, C> Matcher<Triple<A,B,C>,*>.triple_(
    p1: Pattern<A> = any,
    p2: Pattern<B> = any,
    p3: Pattern<C> = any
): Pattern<Triple<A, B, C>> =
    { p1(it.first) && p2(it.second) && p3(it.third) }

/**
 * Matches for a triple when the given pattern for the first element matches.
 *
 * @param A the type of the first element.
 * @param B the type of the second element.
 * @param C the type of the third element.
 * @param pattern the required pattern for the first element.
 * @return the resulting pattern.
 */
fun <A, B, C> triple1(pattern: Pattern<A>): Pattern<Triple<A, B, C>> =
    { pattern(it.first) }

/**
 * Matches for a triple when the given pattern for the second element matches.
 *
 * @param A the type of the first element.
 * @param B the type of the second element.
 * @param C the type of the third element.
 * @param pattern the required pattern for the second element.
 * @return the resulting pattern.
 */
fun <A, B, C> triple2(pattern: Pattern<B>): Pattern<Triple<A, B, C>> =
    { pattern(it.second) }

/**
 * Matches for a triple when the given pattern for the third element matches.
 *
 * @param A the type of the first element.
 * @param B the type of the second element.
 * @param C the type of the third element.
 * @param pattern the required pattern for the third element.
 * @return the resulting pattern.
 */
fun <A, B, C> triple3(pattern: Pattern<C>): Pattern<Triple<A, B, C>> =
    { pattern(it.third) }