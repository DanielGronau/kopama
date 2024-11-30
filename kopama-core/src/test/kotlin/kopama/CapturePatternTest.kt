package kopama

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CapturePatternTest : StringSpec({

    "a capture pattern should succeed and retain the last value" {
        val capInt = capture<Int>()
        capInt(23) shouldBe true
        capInt.value shouldBe 23
        capInt(42) shouldBe true
        capInt.value shouldBe 42
    }

    "a capture pattern should throw an exception when read before matching" {
        val capInt = capture<Int>()
        shouldThrow<UninitializedPropertyAccessException> { capInt.value }
    }

    "capture patterns should work with normal patterns" {
        val capInt = capture<Int>()
        (ge(20) and capInt)(23) shouldBe true
        capInt.value shouldBe 23
        (lt(20) and capInt)(23) shouldBe false
        capInt.value shouldBe 23
    }

    "multiple captures work in one match" {
        match(12 to "twelve") {
            val capInt = capture<Int>()
            val capStr = capture<String>()
            pair(capInt, capStr and hasLength(17)) then { "does not match: ${capInt.value}, ${capStr.value}"}
            pair(capInt, capStr and hasLength(6)) then { "first:${capInt.value} second:${capStr.value}"}
            otherwise { "oops" }
        } shouldBe "first:12 second:twelve"
    }
})