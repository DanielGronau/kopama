package kopama.example

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.Kopama
import kopama.any
import kopama.containsAll
import kopama.eq
import kopama.match

@Kopama
data class GenTest<A : Comparable<A>, B : Any>(val a: A, val b: List<B>)

class GenericTest : StringSpec({

    "generic arguments and arguments with type arguments should be processed correctly" {
        val g = GenTest(23, listOf("x"))

        match(g) {
            genTest(eq(12), any<List<String>>()) then { "nope" }
            genTest(eq(23), containsAll("x")) then { "yupp" }
            otherwise { "oops" }
        } shouldBe "yupp"
    }
})
