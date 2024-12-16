package kopama

class Capture<P : Any> : Pattern<P> {

    lateinit var value: P
        private set

    override fun invoke(obj: P) =
        true.also { value = obj }

    val isSet: Boolean
        get() = this::value.isInitialized

    fun getOrNull(): P? = when {
        this::value.isInitialized -> value
        else -> null
    }
}

inline fun <reified P : Any> capture() = Capture<P>()