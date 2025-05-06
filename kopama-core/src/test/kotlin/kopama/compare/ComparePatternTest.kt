package kopama.compare

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.Serializable

class ComparePatternTest : StringSpec({

    "'isNull' should check if a value is null" {
        isNull(null) shouldBe true
        isNull(23) shouldBe false
    }

    "'isNotNull' should check if a value is null" {
        isNotNull(null) shouldBe false
        isNotNull(23) shouldBe true
    }

    "'isNullOr' should check a pattern if the value is not null" {
        isNullOr(ge(10))(null) shouldBe true
        isNullOr(ge(10))(10) shouldBe true
        isNullOr(ge(10))(9) shouldBe false
    }

    "'isNotNullAnd' should check the values is not null and the given pattern matches" {
        isNotNullAnd(ge(10))(null) shouldBe false
        isNotNullAnd(ge(10))(10) shouldBe true
        isNotNullAnd(ge(10))(9) shouldBe false
    }

    "'eq' should check if a value is equal to a given one" {
        eq(23)(23) shouldBe true
        eq(23)(15) shouldBe false
        eq<Int?>(null)(null) shouldBe true
        eq<Int?>(null)(23) shouldBe false
    }

    "'isSame' should check if a value is the same instance as the given one" {
        class Test

        val t = Test()
        isSame(t)(t) shouldBe true
        isSame(t)(Test()) shouldBe false
    }

    "'oneOf' should check if a value is equal to one of the given ones" {
        oneOf<Int?>()(23) shouldBe false
        oneOf(23)(23) shouldBe true
        oneOf(23)(15) shouldBe false
        oneOf(23, 2, 7, 15, 1234)(15) shouldBe true
        oneOf(23, 2, 7, 16, 1234)(15) shouldBe false
    }

    "'isA' should check if a value is an instance of the given class or interface" {
        isA<String>()("foo") shouldBe true
        isA<CharSequence>()("foo") shouldBe true
        isA<Serializable>()("foo") shouldBe true
        isA<String>()(23) shouldBe false
    }

    "'gt' should check if a comparable value is greater than a given one" {
        gt(15)(23) shouldBe true
        gt(15)(15) shouldBe false
        gt(15)(3) shouldBe false
    }

    "'ge' should check if a comparable value is greater than or equal to a given one" {
        ge(15)(23) shouldBe true
        ge(15)(15) shouldBe true
        ge(15)(3) shouldBe false
    }

    "'lt' should check if a comparable value is less than a given one" {
        lt(15)(23) shouldBe false
        lt(15)(15) shouldBe false
        lt(15)(3) shouldBe true
    }

    "'le' should check if a comparable value is less than or equal to a given one" {
        le(15)(23) shouldBe false
        le(15)(15) shouldBe true
        le(15)(3) shouldBe true
    }

    "'between' should check if a comparable value is between the given values" {
        between(10, 15)(9) shouldBe false
        between(10, 15)(10) shouldBe true
        between(10, 15)(12) shouldBe true
        between(10, 15)(15) shouldBe true
        between(10, 15)(16) shouldBe false
    }

    "'inRange' should check if a comparable value is in the given closed range" {
        inRange(10..15)(9) shouldBe false
        inRange(10..15)(10) shouldBe true
        inRange(10..15)(12) shouldBe true
        inRange(10..15)(15) shouldBe true
        inRange(10..15)(16) shouldBe false

        inRange(10..<15)(9) shouldBe false
        inRange(10..<15)(10) shouldBe true
        inRange(10..<15)(12) shouldBe true
        inRange(10..<15)(15) shouldBe false
        inRange(10..<15)(16) shouldBe false
    }

    "'any' should match everything" {
        any(23) shouldBe true
        any(null) shouldBe true
    }

    "'none' shouldn't match anything" {
        none(23) shouldBe false
        none(null) shouldBe false
    }

    "'any()' should match everything" {
        any<Int?>()(23) shouldBe true
        any<Int?>()(null) shouldBe true
    }

    "'none()' shouldn't match anything" {
        none<Int?>()(23) shouldBe false
        none<Int?>()(null) shouldBe false
    }
})