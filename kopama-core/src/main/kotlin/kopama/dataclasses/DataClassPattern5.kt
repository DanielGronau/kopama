package kopama.dataclasses

import kopama.Pattern
import kopama.compare.any
import kotlin.reflect.KClass

/**
 * Data class pattern of size 5.
 *
 * @param T the data class type.
 * @param A the first parameter type.
 * @param B the second parameter type.
 * @param C the third parameter type.
 * @param D the fourth parameter type.
 * @param E the fifth parameter type.
 * @property kClass the KClass of the data class.
 */
@Suppress("UNCHECKED_CAST")
data class DataClassPattern5<T : Any, A, B, C, D, E>(val kClass: KClass<T>): DataClassPattern(kClass, 5) {

    operator fun invoke(
        comp1: Pattern<A> = any,
        comp2: Pattern<B> = any,
        comp3: Pattern<C> = any,
        comp4: Pattern<D> = any,
        comp5: Pattern<E> = any
    ): Pattern<T?> = { value ->
        value is T &&
                comp1(kClass.java.methods.first { it.name == "component1" }(value) as A) &&
                comp2(kClass.java.methods.first { it.name == "component2" }(value) as B) &&
                comp3(kClass.java.methods.first { it.name == "component3" }(value) as C) &&
                comp4(kClass.java.methods.first { it.name == "component4" }(value) as D) &&
                comp5(kClass.java.methods.first { it.name == "component5" }(value) as E)
    }
}