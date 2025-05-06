package kopama.dataclasses

import kopama.Pattern
import kopama.compare.any
import kotlin.reflect.KClass

/**
 * Data class pattern of size 1.
 *
 * @param T the data class type.
 * @param A the first parameter type.
 * @property kClass the KClass of the data class.
 */
@Suppress("UNCHECKED_CAST")
data class DataClassPattern1<T : Any, A>(val kClass: KClass<T>) {
    operator fun invoke(
        comp1: Pattern<A> = any
    ): Pattern<T?> = { value ->
        value is T &&
                comp1(kClass.java.methods.first { it.name == "component1" }(value) as A)
    }
}