package kopama.dataclasses

import kopama.Pattern
import kopama.compare.any
import kotlin.reflect.KClass

/**
 * Data class pattern of size 2.
 *
 * @param T the data class type.
 * @param A the first parameter type.
 * @param B the second parameter type.
 * @property kClass the KClass of the data class.
 */
@Suppress("UNCHECKED_CAST")
data class DataClassPattern2<T : Any, A, B>(val kClass: KClass<T>): DataClassPattern(kClass, 2) {

    operator fun invoke(
        comp1: Pattern<A> = any,
        comp2: Pattern<B> = any
    ): Pattern<T?> = { value ->
        value is T &&
                comp1(kClass.java.methods.first { it.name == "component1" }(value) as A) &&
                comp2(kClass.java.methods.first { it.name == "component2" }(value) as B)
    }
}