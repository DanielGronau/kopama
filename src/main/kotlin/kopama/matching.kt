package kopama

class Match internal constructor(private val obj: Any?) : Matching {
    override fun equals(other: Any?): Boolean =
        when (other) {
            is Pattern -> other.test(obj)
            else -> false
        }
}

fun match(obj: Any?): Matching = Match(obj)