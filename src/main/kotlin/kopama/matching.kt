package kopama

interface Matching

fun interface Pattern : Matching {
    fun test(obj: Any?): Boolean
}

