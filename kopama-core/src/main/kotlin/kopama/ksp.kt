package kopama

/**
 * Annotation for generating a pattern function for the annotated class using KSP.
 *
 * @property arguments the `val`s, `var`s and zero argument functions to be included in the pattern
 *   (takes `val`s and `var`s from the primary constructor when empty)
 * @property patternName the name of the pattern function (uses the de-capitalized class name when empty)
 * @property fileName the file name for the pattern function (uses the patternName + "Pattern" when empty)
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Kopama(
    val arguments: Array<String> = [],
    val patternName: String = "",
    val fileName: String = "",
)