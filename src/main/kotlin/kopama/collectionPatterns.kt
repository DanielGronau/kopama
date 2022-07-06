package kopama

fun size(size: Int) = object : Pattern {
    override fun test(obj: Any?) = when (obj) {
        is Array<*> -> obj.size == size
        is Collection<*> -> obj.size == size
        is Iterable<*> -> obj.toList().size == size
        is Sequence<*> -> obj.toList().size == size
        is Map<*, *> -> obj.size == size
        is CharSequence -> obj.length == size
        else -> false
    }
}

val empty by lazy { size(0) }

fun contains(elem: Any?) = object : Pattern {
    override fun test(obj: Any?) = when (obj) {
        is Array<*> -> obj.contains(elem)
        is Iterable<*> -> obj.contains(elem)
        is Sequence<*> -> obj.contains(elem)
        is Map<*, *> -> obj.contains(elem)
        is CharSequence -> when (elem) {
            is String -> obj.contains(elem)
            is Char -> obj.contains(elem)
            is Regex -> obj.contains(elem)
            else -> false
        }
        else -> false
    }
}