package kopama.capture

import kopama.Pattern

/**
 * The pattern class for capture.
 *
 * Part of the kopama DSL.
 *
 * @param P the pattern target type.
 * @constructor Create a capture pattern.
 */
class Capture<P> : Pattern<P> {

    private inner class Box(val value: P)

    private var boxedValue: Box? = null

    /**
     * Returns the captured value.
     *
     * @throws UninitializedPropertyAccessException when no value was captured.
     */
    val value: P
        get() = when (val bv = boxedValue) {
            null -> throw UninitializedPropertyAccessException("No value captured")
            else -> bv.value
        }

    override fun invoke(obj: P) =
        true.also { boxedValue = Box(obj) }

    /**
     * Returns true if a value was captured.
     */
    val isSet: Boolean
        get() = boxedValue != null

    /**
     * Returns the captured value, or null if none was captured.
     *
     * Note that captured values may be null as well. In order to distinguish whether
     * no value or a null value was captured, consult the isSet property.
     *
     * @return the captured value, or null if none was captured.
     */
    fun getOrNull(): P? = boxedValue?.value
}

/**
 * Creates a capture pattern for a certain type.
 *
 * Example usage:
 *
 *     val s = match(listOf(1, 42, 3) {
 *        val capInt = capture<Int>()
 *        forAny(capInt and ge(10)) then { "a big number in the list is ${capInt.value}" } //matches
 *        otherwise { "no big number in list" }
 *     }
 *
 * @param P the target type of the capture pattern.
 * @return the resulting pattern.
 */
inline fun <reified P> capture() = Capture<P>()