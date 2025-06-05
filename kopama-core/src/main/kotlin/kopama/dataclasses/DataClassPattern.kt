package kopama.dataclasses

import kotlin.reflect.KClass

/**
 * Convenience function for creating data class patterns of size 1.
 *
 * @param T the data class type.
 * @param A the first parameter type.
 * @return the data class pattern.
 */
inline fun <reified T : Any, A> DataClassPattern(): DataClassPattern1<T, A> =
    DataClassPattern1<T, A>(T::class)

/**
 * Convenience function for creating data class patterns of size 2.
 *
 * @param T the data class type.
 * @param A the first parameter type.
 * @param B the second parameter type.
 * @return the data class pattern.
 */
inline fun <reified T : Any, A, B> DataClassPattern(): DataClassPattern2<T, A, B> =
    DataClassPattern2<T, A, B>(T::class)

/**
 * Convenience function for creating data class patterns of size 3.
 *
 * @param T the data class type.
 * @param A the first parameter type.
 * @param B the second parameter type.
 * @param C the third parameter type.
 * @return the data class pattern.
 */
inline fun <reified T : Any, A, B, C> DataClassPattern(): DataClassPattern3<T, A, B, C> =
    DataClassPattern3<T, A, B, C>(T::class)

/**
 * Convenience function for creating data class patterns of size 4.
 *
 * @param T the data class type.
 * @param A the first parameter type.
 * @param B the second parameter type.
 * @param C the third parameter type.
 * @param D the fourth parameter type.
 * @return the data class pattern.
 */
inline fun <reified T : Any, A, B, C, D> DataClassPattern(): DataClassPattern4<T, A, B, C, D> =
    DataClassPattern4<T, A, B, C, D>(T::class)

/**
 * Convenience function for creating data class patterns of size 5.
 *
 * @param T the data class type.
 * @param A the first parameter type.
 * @param B the second parameter type.
 * @param C the third parameter type.
 * @param D the fourth parameter type.
 * @param E the fifth parameter type.
 * @return the data class pattern.
 */
inline fun <reified T : Any, A, B, C, D, E> DataClassPattern(): DataClassPattern5<T, A, B, C, D, E> =
    DataClassPattern5<T, A, B, C, D, E>(T::class)

/**
 * Convenience function for creating data class patterns of size 6.
 *
 * @param T the data class type.
 * @param A the first parameter type.
 * @param B the second parameter type.
 * @param C the third parameter type.
 * @param D the fourth parameter type.
 * @param E the fifth parameter type.
 * @param F the sixth parameter type.
 * @return the data class pattern.
 */
inline fun <reified T : Any, A, B, C, D, E, F> DataClassPattern(): DataClassPattern6<T, A, B, C, D, E, F> =
    DataClassPattern6<T, A, B, C, D, E, F>(T::class)

/**
 * Convenience function for creating data class patterns of size 7.
 *
 * @param T the data class type.
 * @param A the first parameter type.
 * @param B the second parameter type.
 * @param C the third parameter type.
 * @param D the fourth parameter type.
 * @param E the fifth parameter type.
 * @param F the sixth parameter type.
 * @param G the seventh parameter type.
 * @return the data class pattern.
 */
inline fun <reified T : Any, A, B, C, D, E, F, G> DataClassPattern(): DataClassPattern7<T, A, B, C, D, E, F, G> =
    DataClassPattern7<T, A, B, C, D, E, F, G>(T::class)

/**
 * Convenience function for creating data class patterns of size 8.
 *
 * @param T the data class type.
 * @param A the first parameter type.
 * @param B the second parameter type.
 * @param C the third parameter type.
 * @param D the fourth parameter type.
 * @param E the fifth parameter type.
 * @param F the sixth parameter type.
 * @param G the seventh parameter type.
 * @param H the eighth parameter type.
 * @return the data class pattern.
 */
inline fun <reified T : Any, A, B, C, D, E, F, G, H> DataClassPattern(): DataClassPattern8<T, A, B, C, D, E, F, G, H> =
    DataClassPattern8<T, A, B, C, D, E, F, G, H>(T::class)

abstract class DataClassPattern(kClass: KClass<*>, arity: Int) {
    init {
        val hasComponentNs = (1..arity).map { n ->
            kClass.java.methods.any { m -> m.name == "component$n" && m.parameters.size == 0 }
        }.reduce(Boolean::and)
        require(hasComponentNs) { "'${kClass.simpleName}' is not a data class or has not enough arguments" }
    }
}