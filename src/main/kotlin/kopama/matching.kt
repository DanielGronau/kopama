package kopama

interface Matching

interface Pattern : Matching {
    fun test(obj: Any?): Boolean
}
class Match internal constructor(private val obj: Any?) : Matching {
    override fun equals(other: Any?): Boolean =
        when (other) {
            is Pattern -> other.test(obj)
            else -> false
        }

    override fun hashCode() = 0
}

fun match(obj: Any?): Matching = Match(obj)

