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

val any = object : Pattern {
    override fun test(obj: Any?) = true
}

fun eq(value: Any?) = object : Pattern {
    override fun test(obj: Any?) = value == obj
}

fun oneOf(vararg values: Any?) = object : Pattern {
    override fun test(obj: Any?) = values.any { it == obj }
}

