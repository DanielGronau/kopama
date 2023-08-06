package kopama

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CaptureTest : StringSpec({

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

})