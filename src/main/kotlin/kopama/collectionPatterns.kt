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

fun all(pattern: Pattern) = object : Pattern {
    override fun test(obj: Any?) = when (obj) {
        is Array<*> -> obj.all{ pattern.test(it) }
        is Iterable<*> -> obj.all{ pattern.test(it) }
        is Sequence<*> -> obj.all{ pattern.test(it) }
        is CharSequence -> obj.all{ pattern.test(it) }
        else -> false
    }
}

fun exists(pattern: Pattern) = object : Pattern {
    override fun test(obj: Any?) = when (obj) {
        is Array<*> -> obj.any{ pattern.test(it) }
        is Iterable<*> -> obj.any{ pattern.test(it) }
        is Sequence<*> -> obj.any{ pattern.test(it) }
        is CharSequence -> obj.any{ pattern.test(it) }
        else -> false
    }
}

fun none(pattern: Pattern) = object : Pattern {
    override fun test(obj: Any?) = when (obj) {
        is Array<*> -> obj.none{ pattern.test(it) }
        is Iterable<*> -> obj.none{ pattern.test(it) }
        is Sequence<*> -> obj.none{ pattern.test(it) }
        is CharSequence -> obj.none{ pattern.test(it) }
        else -> false
    }
}

fun allKeys(pattern: Pattern) = object : Pattern {
    override fun test(obj: Any?) = when(obj) {
        is Map<*,*> -> obj.keys.all { pattern.test(it) }
        else -> false
    }
}

fun existKeys(pattern: Pattern) = object : Pattern {
    override fun test(obj: Any?) = when(obj) {
        is Map<*,*> -> obj.keys.any { pattern.test(it) }
        else -> false
    }
}

fun noKey(pattern: Pattern) = object : Pattern {
    override fun test(obj: Any?) = when(obj) {
        is Map<*,*> -> obj.keys.none { pattern.test(it) }
        else -> false
    }
}

fun allValues(pattern: Pattern) = object : Pattern {
    override fun test(obj: Any?) = when(obj) {
        is Map<*,*> -> obj.values.all { pattern.test(it) }
        else -> false
    }
}

fun existsValue(pattern: Pattern) = object : Pattern {
    override fun test(obj: Any?) = when(obj) {
        is Map<*,*> -> obj.values.any { pattern.test(it) }
        else -> false
    }
}

fun noValue(pattern: Pattern) = object : Pattern {
    override fun test(obj: Any?) = when(obj) {
        is Map<*,*> -> obj.values.none { pattern.test(it) }
        else -> false
    }
}