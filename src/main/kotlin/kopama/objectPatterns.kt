package kopama

import kotlin.reflect.KClass

val isNull by lazy {
    Pattern { obj -> obj == null }
}

fun isA(kClass: KClass<*>) = Pattern { obj -> kClass.isInstance(obj) }

fun isA(klass: Class<*>) = Pattern { obj -> klass.isInstance(obj) }

fun isSame(instance: Any?) = Pattern { obj -> obj === instance }

fun hasToString(string: String) = Pattern { obj -> obj.toString() == string }

val any by lazy {
    Pattern { true }
}

fun eq(value: Any?) = Pattern { obj -> value == obj }

inline fun <reified T : Comparable<T>> gt(value: T) = Pattern { obj ->
    when (obj) {
        is T -> obj > value
        else -> false
    }
}

inline fun <reified T : Comparable<T>> ge(value: T) = Pattern { obj ->
    when (obj) {
        is T -> obj >= value
        else -> false
    }
}

inline fun <reified T : Comparable<T>> lt(value: T) = Pattern { obj ->
    when (obj) {
        is T -> obj < value
        else -> false
    }
}

inline fun <reified T : Comparable<T>> le(value: T) = Pattern { obj ->
    when (obj) {
        is T -> obj <= value
        else -> false
    }
}

fun oneOf(vararg values: Any?) = Pattern { obj -> values.any { it == obj } }

