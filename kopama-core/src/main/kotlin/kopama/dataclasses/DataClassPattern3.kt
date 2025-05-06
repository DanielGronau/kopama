package kopama.dataclasses

import kopama.Pattern
import kopama.compare.any
import kotlin.reflect.KClass

/**
 * Data class pattern of size 3.
 *
 * @param T the data class type.
 * @param A the first parameter type.
 * @param B the second parameter type.
 * @param C the third parameter type.
 * @property kClass the KClass of the data class.
 */
@Suppress("UNCHECKED_CAST")
data class DataClassPattern3<T : Any, A, B, C>(val kClass: KClass<T>) {
    operator fun invoke(
        comp1: Pattern<A> = any,
        comp2: Pattern<B> = any,
        comp3: Pattern<C> = any
    ): Pattern<T?> = { value ->
        value is T &&
                comp1(kClass.java.methods.first { it.name == "component1" }(value) as A) &&
                comp2(kClass.java.methods.first { it.name == "component2" }(value) as B) &&
                comp3(kClass.java.methods.first { it.name == "component3" }(value) as C)
    }
}