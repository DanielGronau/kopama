package kopama

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Kopama(
    val patternName: String = "", // default: decapitalized name of the class
    val patternFileName: String = "", // default: patterName + "Pattern"
)