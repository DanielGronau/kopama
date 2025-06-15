package kopama.example

import kopama.Kopama
import java.time.Instant

@Kopama(arguments = ["foo", "bar", "baz", "quux"])
class ArgTest(val foo: Int, var bar: List<String>) {
    val baz = 12.1
    fun quux(): String = "345"
}


enum class Gender { MALE, FEMALE, DIVERSE }

sealed interface LegalEntity

@Kopama
data class Person(val firstName: String, val lastName: String, val age: Int, val gender: Gender) : LegalEntity

@Kopama
class Company(val companyName: String, var taxID: String, nonsense: String) : LegalEntity

@Kopama
data class GenTest<A : Comparable<A>, B : Any>(val a: A, val b: List<B>)

@Kopama(patternName = "bar", fileName = "bazPattern")
data class Foo(val alpha: Int, val beta: String, val gamma: Instant)

@Kopama
data class Outer(val inner: Inner, val outers: List<Outer>)

@JvmInline
@Kopama
value class Inner(val value: Int)

@Kopama
data class NullTest(val alpha: Int, val beta: Int?, var gamma: String, var delta: String?)