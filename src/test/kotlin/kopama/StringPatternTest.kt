package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class StringPatternTest : StringSpec({

    "eqIgnoreCase(String) should compare Strings case-insensitive" {
        eqIgnoreCase("Bob").test("bOb") shouldBe true
        eqIgnoreCase("Bob").test("Alice") shouldBe false
        eqIgnoreCase("Bob").test(null) shouldBe false
        eqIgnoreCase("Bob").test(42) shouldBe false
    }

    "startsWith(String) should test whether the original String starts with the given one" {
        startsWith("cat").test("caterpillar") shouldBe true
        startsWith("cat").test("bobcat") shouldBe false
        startsWith("cat").test("scatter") shouldBe false
        startsWith("cat").test("cart") shouldBe false
        startsWith("cat").test(null) shouldBe false
        startsWith("cat").test(42) shouldBe false
    }

    "endsWith(String) should test whether the original String ends with the given one" {
        endsWith("cat").test("caterpillar") shouldBe false
        endsWith("cat").test("bobcat") shouldBe true
        endsWith("cat").test("scatter") shouldBe false
        endsWith("cat").test("cart") shouldBe false
        endsWith("cat").test(null) shouldBe false
        endsWith("cat").test(42) shouldBe false
    }

    "regex(String) should test whether the original String matches the given regex" {
        regex("cat").test("cat") shouldBe true
        regex("[a-h]at").test("cat") shouldBe true
        regex("[i-z]at").test("cat") shouldBe false
        regex("[i-z]at").test(null) shouldBe false
        regex("[i-z]at").test(34) shouldBe false
    }
})
