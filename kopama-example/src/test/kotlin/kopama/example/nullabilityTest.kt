package kopama.example

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.Kopama
import kopama.compare.isNotNull
import kopama.compare.isNull
import kopama.match

@Kopama
data class NullTest(val alpha: Int, val beta: Int?, var gamma: String, var delta: String?)

class NullabilityTest : StringSpec({

    "nullable values generate nullable patterns" {
        match(NullTest(1, null, "a", null)) {
            isNull then { "very null" }
            nullTest(isNotNull, isNull, isNotNull, isNull) then { "yupp" }
            otherwise { "oops" }
        } shouldBe "yupp"
    }
})