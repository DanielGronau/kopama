package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.Serializable

class OperatorPatternTest : StringSpec({

    "'!' should invert the result of a pattern" {
        !isNull(23) shouldBe true
        !isNull(null) shouldBe false
    }

    "'and' should only succeed if both pattern operands succeed" {
        (any and isNull)(null) shouldBe true
        (any and isNull)(23) shouldBe false
        (isNull and any)(23) shouldBe false
        (isNull and none)(23) shouldBe false
    }

    "'or' should succeed if any of the pattern operands succeed" {
        (any or isNull)(null) shouldBe true
        (any or isNull)(23) shouldBe true
        (isNull or any)(23) shouldBe true
        (isNull or none)(23) shouldBe false
    }

    "'xor' should succeed if exactly one of the pattern operands succeed" {
        (any xor isNull)(null) shouldBe false
        (any xor isNull)(23) shouldBe true
        (isNull xor any)(23) shouldBe true
        (isNull xor none)(23) shouldBe false
    }

    "'allOf' should succeed if all pattern arguments succeed" {
        allOf<Int?>()(23) shouldBe true
        allOf(any)(23) shouldBe true
        allOf(isNull)(23) shouldBe false
        allOf(any, isNull, any)(null) shouldBe true
        allOf(any, isNull, any)(23) shouldBe false
    }

    "'anyOf' should succeed of any of the pattern arguments succeeds" {
        anyOf<Int?>()(null) shouldBe false
        anyOf(any)(23) shouldBe true
        anyOf(any, isNull, none)(23) shouldBe true
        anyOf(none, isNull, none)(23) shouldBe false
    }

    "'noneOf' should succeed if none of the pattern arguments succeeds" {
        noneOf<Int?>()(23) shouldBe true
        noneOf(any)(23) shouldBe false
        noneOf(isNull)(23) shouldBe true
        noneOf(any, isNull, any)(null) shouldBe false
        noneOf(none, isNull, none)(23) shouldBe true
    }

    "'on' should extract a value and apply it to a pattern" {
        (isNull on { s: String? -> s?.toInt() })(null) shouldBe true
        (isNull on { s: String? -> s?.toInt() })("23") shouldBe false
    }

    "'thenRequire' should match a pattern if a precondition is met" {
        (ge(7) thenRequire eq(24))(24) shouldBe true
        (ge(7) thenRequire eq(24))(23) shouldBe false
        (ge(7) thenRequire eq(24))(2) shouldBe true
    }

    "'ifThenElse' should match one of the given patterns according to the given precondition" {
        ifThenElse(ge(10), eq(12), eq(8))(12) shouldBe true
        ifThenElse(ge(10), eq(12), eq(8))(13) shouldBe false
        ifThenElse(ge(10), eq(12), eq(8))(8) shouldBe true
        ifThenElse(ge(10), eq(12), eq(8))(7) shouldBe false
    }
})