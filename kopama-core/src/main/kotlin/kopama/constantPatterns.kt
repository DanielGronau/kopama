package kopama

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
