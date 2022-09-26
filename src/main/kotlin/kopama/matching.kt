package kopama

interface Matching

fun interface Pattern : Matching {
    fun test(obj: Any?): Boolean
}
fun match(obj: Any?): Matching = object : Matching {
    override fun equals(other: Any?) =
        when (other) {
            is Pattern -> other.test(obj)
            else -> false
        }
}

