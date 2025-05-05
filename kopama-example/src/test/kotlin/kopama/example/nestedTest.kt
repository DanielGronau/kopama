package kopama.example

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.Kopama
import kopama.compare.any
import kopama.collections.forAny
import kopama.compare.eq
import kopama.collections.isEmpty
import kopama.match

@Kopama
data class Outer(val inner: Inner, val outers: List<Outer>)

@JvmInline
@Kopama
value class Inner(val value: Int)

class NestedTest : StringSpec({

    "nested patterns work" {
        val o1 = Outer(Inner(27), emptyList())
        val o2 = Outer(Inner(36), emptyList())
        val o3 = Outer(Inner(42), listOf(o1, o2))

        match(o2) {
            outer(inner(eq(36)), isEmpty() ) then { "yupp o2" }
            otherwise { "nope" }
        } shouldBe "yupp o2"

        match(o3) {
            outer(inner = +inner(eq(12)), outers = any) then { "nope" }
            outer(inner(eq(42)), forAny(outer(inner(eq(36)), isEmpty()))) then { "yupp o3" }
            otherwise { "oops" }
        } shouldBe "yupp o3"
    }
})