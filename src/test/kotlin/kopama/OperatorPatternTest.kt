package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class OperatorPatternTest : StringSpec({

    "'!' should negate the pattern result" {
        !eq("Bob").test("Bob") shouldBe false
        !eq("Bob").test("Alice") shouldBe true
    }

    "and should match when both of the patterns match" {
        (eq("Alice") and eq("Bob")).test("Alice") shouldBe false
        (eq("Alice") and eq("Bob")).test("Bob") shouldBe false
        (eq("Alice") and eq("Bob")).test("Charlie") shouldBe false
        (eq("Alice") and eq("Alice")).test("Alice") shouldBe true
    }

    "or should match when at least one of the patterns matches" {
        (eq("Alice") or eq("Bob")).test("Alice") shouldBe true
        (eq("Alice") or eq("Bob")).test("Bob") shouldBe true
        (eq("Alice") or eq("Bob")).test("Charlie") shouldBe false
        (eq("Alice") or eq("Alice")).test("Alice") shouldBe true
    }

    "xor should match when exactly one of the patterns matches" {
        (eq("Alice") xor eq("Bob")).test("Alice") shouldBe true
        (eq("Alice") xor eq("Bob")).test("Bob") shouldBe true
        (eq("Alice") xor eq("Bob")).test("Charlie") shouldBe false
        (eq("Alice") xor eq("Alice")).test("Alice") shouldBe false
    }

    "allOf should match if all argument patterns match" {
        allOf().test("Alice") shouldBe true
        allOf(eq("Alice"), any, !isNull).test("Alice") shouldBe true
        allOf(eq("Alice"), any, eq("Bob"), !isNull).test("Alice") shouldBe false
    }

    "anyOf should match if all argument patterns match" {
        anyOf().test("Alice") shouldBe false
        anyOf(eq("Alice"), eq("Bob"), isNull).test("Alice") shouldBe true
        anyOf(eq("Alice"), eq("Bob"), isNull).test("Bob") shouldBe true
        anyOf(eq("Alice"), eq("Bob"), isNull).test("Charlie") shouldBe false
    }
})

