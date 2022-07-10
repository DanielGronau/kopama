package kopama

import kotlin.reflect.KClass
import kotlin.reflect.cast

class Capture<T : Any>(val kclass: KClass<T>) : Pattern {

    lateinit var value: T
        private set

    override fun test(obj: Any?) = when {
        kclass.isInstance(obj) -> true.also { value = kclass.cast(obj) }
        else -> false
    }
}

inline fun <reified T : Any> capture() = Capture(T::class)