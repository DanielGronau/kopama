package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SimplePatternTest : StringSpec({

    "isNull should test for null values" {
        isNull.test("23") shouldBe false
        isNull.test(null) shouldBe true
    }

    "eq() should test equality" {
        eq("Bob").test(null) shouldBe false
        eq("Bob").test(0) shouldBe false
        eq("Bob").test("Alice") shouldBe false
        eq("Bob").test("Bob") shouldBe true

        eq(null).test("Bob") shouldBe false
        eq(null).test(null) shouldBe true
    }

    "any should match anything" {
        any.test(null) shouldBe true
        any.test(0) shouldBe true
        any.test("Bob") shouldBe true
    }

    "oneOf() should test equality with at least one of the given values" {
        oneOf().test("Bob") shouldBe false
        oneOf(1, null, "Alice", 'c').test("Bob") shouldBe false
        oneOf(1, null, "Bob", 'c').test("Bob") shouldBe true
        oneOf("Bob", "Alice", "Bob", "Bob").test("Bob") shouldBe true

        oneOf(1, 0.0, "Bob", 'c').test(null) shouldBe false
        oneOf(1, null, "Bob", 'c').test(null) shouldBe true
    }
})
