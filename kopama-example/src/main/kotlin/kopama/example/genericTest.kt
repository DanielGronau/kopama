package kopama.example

import kopama.Kopama
import kopama.any
import kopama.containsAll
import kopama.eq
import kopama.match

@Kopama
data class GenTest<A : Comparable<A>, B : Any>(val a: A, val b: List<B>)

fun main() {
    val g = GenTest(23, listOf("x"))

    val s = match(g) {
        genTest(eq(12), any<List<String>>()) then { "nope" }
        genTest(eq(23), containsAll("x")) then { "yupp" }
        otherwise { "oops" }
    }

    println(s)
}
