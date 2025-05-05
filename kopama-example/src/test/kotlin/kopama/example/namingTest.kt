package kopama.example

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.Kopama
import kopama.compare.any
import kopama.compare.eq
import kopama.match
import java.time.Instant

@Kopama(patternName = "bar", fileName = "bazPattern")
data class Foo(val alpha: Int, val beta: String, val gamma: Instant)


class NamingTest : StringSpec({

    "pattern can be renamed via annotation" {
        match(Foo(12, "twelve", Instant.now())) {
            bar(eq(12), +"eleven", any) then { "nope" }
            bar(eq(12), +"twelve", any) then { "yupp" }
            otherwise { "oops" }
        } shouldBe "yupp"
    }
})

