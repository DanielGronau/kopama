package kopama

class Capture<P> : Pattern<P> {

    private inner class Box(val value: P)

    private var boxedValue: Box? = null

    val value: P
        get() = when (val bv = boxedValue) {
            null -> throw UninitializedPropertyAccessException("No value captured")
            else -> bv.value
        }

    override fun invoke(obj: P) =
        true.also { boxedValue = Box(obj) }

    val isSet: Boolean
        get() = boxedValue != null

    fun getOrNull(): P? = boxedValue?.value
}

inline fun <reified P> capture() = Capture<P>()