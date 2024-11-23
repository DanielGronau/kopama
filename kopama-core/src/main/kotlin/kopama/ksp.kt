package kopama

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Kopama(
    val arguments: Array<String> = [], // default: try to derive from class
    val patternName: String = "", // default: decapitalized name of the class
    val fileName: String = "", // default: patterName + "Pattern"
)