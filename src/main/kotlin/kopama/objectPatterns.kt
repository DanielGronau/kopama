package kopama

import kotlin.reflect.KClass

val isNull by lazy {
    object : Pattern {
        override fun test(obj: Any?) = obj == null
    }
}

fun isA(kClass: KClass<*>) = object : Pattern {
    override fun test(obj: Any?) = kClass.isInstance(obj)
}

fun isA(klass: Class<*>) = object : Pattern {
    override fun test(obj: Any?) = klass.isInstance(obj)
}

fun isSame(instance: Any?) = object : Pattern {
    override fun test(obj: Any?) = obj === instance
}

fun hasToString(string: String) = object : Pattern {
    override fun test(obj: Any?) = obj.toString() == string
}

val any by lazy {
    object : Pattern {
        override fun test(obj: Any?) = true
    }
}

fun eq(value: Any?) = object : Pattern {
    override fun test(obj: Any?) = value == obj
}

inline fun <reified T : Comparable<T>> gt(value: T) = object : Pattern {
    override fun test(obj: Any?) = when (obj) {
        is T -> obj > value
        else -> false
    }
}

inline fun <reified T : Comparable<T>> ge(value: T) = object : Pattern {
    override fun test(obj: Any?) = when (obj) {
        is T -> obj >= value
        else -> false
    }
}

inline fun <reified T : Comparable<T>> lt(value: T) = object : Pattern {
    override fun test(obj: Any?) = when (obj) {
        is T -> obj < value
        else -> false
    }
}

inline fun <reified T : Comparable<T>> le(value: T) = object : Pattern {
    override fun test(obj: Any?) = when (obj) {
        is T -> obj <= value
        else -> false
    }
}

fun oneOf(vararg values: Any?) = object : Pattern {
    override fun test(obj: Any?) = values.any { it == obj }
}

