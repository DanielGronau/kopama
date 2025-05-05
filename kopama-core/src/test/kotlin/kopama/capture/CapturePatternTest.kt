package kopama.capture

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.operators.and
import kopama.collections.forAny
import kopama.compare.ge
import kopama.strings.hasLength
import kopama.compare.isNotNullAnd
import kopama.compare.lt
import kopama.match
import kopama.tuples.pair

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

    "a capture pattern should know if it was set" {
        val capInt = capture<Int>()
        capInt.isSet shouldBe false
        capInt(12)
        capInt.isSet shouldBe true
    }

    "getOrNull() should give save access" {
        val capInt = capture<Int>()
        capInt.getOrNull() shouldBe null
        capInt(12)
        capInt.getOrNull() shouldBe 12
    }

    "capture patterns should combine with normal patterns" {
        val capInt = capture<Int>()
        (ge(20) and capInt)(23) shouldBe true
        capInt.value shouldBe 23
        (lt(20) and capInt)(25) shouldBe false
        capInt.value shouldBe 23 // not evaluated
    }

    "capture patterns can be wrapped in isNotNullAnd()" {
        val capInt = capture<Int>()
        isNotNullAnd(capInt)(null) shouldBe false
        capInt.isSet shouldBe false
        isNotNullAnd(capInt)(25) shouldBe true
        capInt.value shouldBe 25
    }

    "capture patterns work with nullable types" {
        val capInt = capture<String?>()
        capInt(null) shouldBe true
        capInt.value shouldBe null
        capInt("x") shouldBe true
        capInt.value shouldBe "x"
    }

    "multiple captures work in one match" {
        match(12 to "twelve") {
            val capInt = capture<Int>()
            val capStr = capture<String>()
            pair(capInt, capStr and hasLength(17)) then
                    { "does not match: ${capInt.value}, ${capStr.value}" }
            pair(capInt, capStr and hasLength(6)) then
                    { "first:${capInt.value} second:${capStr.value}" }
            otherwise { "oops" }
        } shouldBe "first:12 second:twelve"
    }

    "can capture elements from list" {
        match(listOf(1, 2, 30, 4, 5)) {
            val capInt = capture<Int>()
            forAny(capInt and ge(10)) then { "big value is ${capInt.value}" }
            otherwise { "no big value" }
        } shouldBe "big value is 30"
    }

})