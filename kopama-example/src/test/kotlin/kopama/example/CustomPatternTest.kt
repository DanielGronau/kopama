package kopama.example

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.match
import kopama.tuples.pair_

class CustomPatternTest : StringSpec({

    "using on custom patterns works" {
        val p = "1a" to 42

        match(p) {
            pair_({ it[0].isDigit() }, { it % 2 == 1 }) then { "nope" }
            pair_({ it[1].isLetter() }, { it % 2 == 0 }) then { "yupp" }
            otherwise { "oops" }
        } shouldBe "yupp"
    }
})