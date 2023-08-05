package kopama

class Capture<P : Any> : Pattern<P> {

    lateinit var value: P
        private set

    override fun invoke(obj: P) =
        true.also { value = obj }
}

inline fun <reified P : Any> capture() = Capture<P>()