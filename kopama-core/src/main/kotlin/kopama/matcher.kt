package kopama

typealias Pattern<P> = (P) -> Boolean

/**
 * The `MatchResult` interface.
 *
 * Part of the kopama DSL. This interface shouldn't be instantiated.
 *
 * @param T the result type of the match block.
 */
interface MatchResult<T : Any> {
    val value: T
}

/**
 * The `Matcher` class.
 *
 * Part of the kopama DSL. This class shouldn't be instantiated.
 *
 * @param P the type of the target object.
 * @param T the result type of the match block.
 * @property obj the target object.
 * @constructor don't instantiate the class directly.
 */
class Matcher<P, T : Any>(val obj: P) {

    var result: T? = null

    /**
     * Constructs a clause inside a match block.
     *
     * The general syntax is `<pattern> then { <result value> }`
     *
     * @param Q
     * @param value
     * @receiver
     */
    inline infix fun <reified Q : P> Pattern<Q>.then(value: () -> T) {
        if (result == null && obj is Q && this(obj)) {
            result = value()
        }
    }

    /**
     * A shortcut for `eq`.
     *
     * E.g. `+"abc"` is the same as writing `eq("abc")`.
     */
    operator fun Any.unaryPlus() = eq(this)

    /**
     * Constructs the default clause inside a match block.
     *
     * The general syntax is `otherwise { <default value> }`, which must be the last clause in the block.
     *
     * @param default
     * @receiver
     */
    fun otherwise(default: () -> T) = object : MatchResult<T> {
        override val value = result ?: default()
    }
}

/**
 * Creates a match block.
 *
 * Part of the kopama DSL. The general syntax is:
 *
 * ```
 *  val result = match(<target object>) {
 *     <pattern> then { <result value> }
 *     <pattern> then { <result value> }
 *     <pattern> then { <result value> }
 *     ...
 *     otherwise { <default value> }
 * }
 * ```
 *
 * @param P the type of the target object.
 * @param T the result type of the match block.
 * @param obj the target object.
 * @param body the match block body.
 * @return the result of the match block.
 */
fun <P, T : Any> match(obj: P, body: Matcher<P, T>.() -> MatchResult<T>): T =
    Matcher<P, T>(obj).run(body).value