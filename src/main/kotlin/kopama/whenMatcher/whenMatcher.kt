package kopama.whenMatcher

import kopama.Matching
import kopama.Pattern
import kotlin.reflect.KClass
import kotlin.reflect.cast

fun match(obj: Any?): Matching = object : Matching {
    override fun equals(other: Any?) =
        when (other) {
            is Pattern -> other.test(obj)
            else -> false
        }
}

class Capture<T : Any>(val kclass: KClass<T>) : Pattern {

    lateinit var value: T
        private set

    override fun test(obj: Any?) = when {
        kclass.isInstance(obj) -> true.also { value = kclass.cast(obj) }
        else -> false
    }
}

inline fun <reified T : Any> capture() = Capture(T::class)

