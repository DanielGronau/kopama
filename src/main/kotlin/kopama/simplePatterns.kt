package kopama

val isNull by lazy {
    object : Pattern {
        override fun test(obj: Any?) = obj == null
    }
}

val any = object : Pattern {
    override fun test(obj: Any?) = true
}

fun eq(value: Any?) = object : Pattern {
    override fun test(obj: Any?) = value == obj
}

fun oneOf(vararg values: Any?) = object : Pattern {
    override fun test(obj: Any?) = values.any { it == obj }
}

