package kopama

import kopama.compare.eq

/**
 * The `ValidationResult` class.
 *
 * Part of the kopama DSL.
 *
 * @param F the result type of a validation failure.
 */
data class ValidationResult<F>(val failures: List<F>) {

    operator fun plus(failure: F): ValidationResult<F> =
        ValidationResult(failures + failure)

    operator fun plus(that: ValidationResult<F>): ValidationResult<F> =
        ValidationResult(this.failures + that.failures)

    fun isValid(): Boolean = failures.isEmpty()

    fun onFailure(action: (List<F>) -> Unit) {
        if (! isValid()) action(failures)
    }

    override fun toString(): String = when {
        isValid() -> "Valid()"
        else -> "Invalid(${failures.joinToString()})"
    }
}

/**
 * Smart constructor for a valid `ValidationResult`.
 *
 * @param F
 */
fun <F> Valid() = ValidationResult<F>(emptyList())

/**
 * Smart constructor for an invalid `ValidationResult`.
 *
 * @param F
 */
fun <F> Invalid(first: F, vararg failures: F) = ValidationResult(listOf(first) + failures)

/**
 * The `Validator` class.
 *
 * Part of the kopama DSL. This class shouldn't be instantiated.
 *
 * @param P the type of the target object.
 * @param F the type of a validation failure.
 * @property obj the target object.
 * @constructor don't instantiate the class directly.
 */
class Validator<P, F : Any>(val obj: P) {

    var result: ValidationResult<F> = Valid()

    /**
     * Constructs a clause inside a validation block.
     *
     * The general syntax is `<pattern> then { <validation failure> }`
     *
     * @param Q
     * @param failure
     * @receiver
     */
    inline infix fun <reified Q : P> Pattern<Q>.onFail(failure: () -> F) {
        if (obj !is Q || !this(obj)) {
            result += failure()
        }
    }

    /**
     * A shortcut for `eq`.
     *
     * E.g. `+"abc"` is the same as writing `eq("abc")`.
     */
    operator fun Any.unaryPlus() = eq(this)

}

/**
 * Creates a validation block.
 *
 * Part of the kopama DSL. The general syntax is:
 *
 * ```
 *  val result = validate(<target object>) {
 *     <pattern> onFail { <validation failure> }
 *     <pattern> onFail { <validation failure> }
 *     <pattern> onFail { <validation failure> }
 * }
 * ```
 *
 * @param P the type of the target object.
 * @param T the result type of the match block.
 * @param obj the target object.
 * @param body the match block body.
 * @return the result of the match block.
 */
fun <P, T : Any> validate(obj: P, body: Validator<P, T>.() -> Unit): ValidationResult<T> =
    Validator<P, T>(obj).also(body).result