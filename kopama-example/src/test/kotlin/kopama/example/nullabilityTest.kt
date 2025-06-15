package kopama.example

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.compare.isNotNull
import kopama.compare.isNull
import kopama.match

class NullabilityTest : StringSpec({

    "nullable values generate nullable patterns" {
        match(NullTest(1, null, "a", null)) {
            isNull then { "very null" }
            nullTest(isNotNull, isNull, isNotNull, isNull) then { "yupp" }
            otherwise { "oops" }
        } shouldBe "yupp"
    }
})