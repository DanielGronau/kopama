package kopama

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kopama.compare.gt
import kopama.compare.lt
import kopama.strings.endsWith
import kopama.strings.hasLength
import kopama.strings.startsWith

class ValidatorTest : StringSpec({

    "a validator should return all failing cases" {
        validate("foo") {
            hasLength(4) onFail { "must have length 4" }
            startsWith("fo") onFail { "must start with fo" }
            endsWith("of") onFail { "must end with of" }
        } shouldBe Invalid("must have length 4", "must end with of")
    }

    "a validator should be valid if no case matches" {
        validate("foo") {
            hasLength(3) onFail { "must have length 3" }
            startsWith("fo") onFail { "must start with fo" }
            endsWith("oo") onFail { "must end with oo" }
        }.isValid() shouldBe true
    }

    "an empty validator should be valid" {
        validate<_, String>("foo") {
        }.isValid() shouldBe true
    }

    "a validator should fail on patterns with the wrong type" {
        val n: Number = 42
        validate(n) {
            lt(50) onFail { "must be smaller than 50" }
            gt(40.0) onFail { "must be bigger than 40.0" }
        } shouldBe Invalid("must be bigger than 40.0")
    }

    "isValid results should return the correct status" {
        Valid<String>().isValid() shouldBe true
        Invalid("a", "b").isValid() shouldBe false
    }

    "validation result should return the correct failures" {
        Valid<String>().failures shouldHaveSize 0
        Invalid("a", "b").failures shouldBe listOf("a", "b")
    }

    "failures can be added to validation results" {
        (Valid<String>() + "oops") shouldBe Invalid("oops")
        (Invalid("a", "b") + "c") shouldBe Invalid("a", "b", "c")
    }

    "validation results can be combined" {
        val valid = Valid<String>()
        val invalid1 = Invalid("a", "b")
        val invalid2 = Invalid("c", "d")
        (valid + valid) shouldBe valid
        (invalid1 + valid) shouldBe invalid1
        (valid + invalid2) shouldBe invalid2
        (invalid1 + invalid2) shouldBe Invalid("a", "b", "c", "d")
    }

    "validation result onFailure works" {
        Valid<String>().onFailure { error { "should not happen" } }

        shouldThrow<IllegalArgumentException> {
            Invalid("a", "b", "c").onFailure {
                throw IllegalArgumentException("Problems: $it")
            }
        }.message shouldBe "Problems: [a, b, c]"
    }
})