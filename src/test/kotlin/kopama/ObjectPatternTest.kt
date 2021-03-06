package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ObjectPatternTest : StringSpec({

    "isNull should test for null values" {
        isNull.test("23") shouldBe false
        isNull.test(null) shouldBe true
    }

    "isA(KClass) should test if obj is an instance" {
        isA(String::class).test(null) shouldBe false
        isA(String::class).test(42) shouldBe false
        isA(String::class).test("Alice") shouldBe true
        isA(CharSequence::class).test("Alice") shouldBe true

        isA(Int::class).test(42) shouldBe true
        isA(Number::class).test(42) shouldBe true
    }

    "isA(Class) should test if obj is an instance" {
        isA(String::class.java).test(null) shouldBe false
        isA(String::class.java).test(42) shouldBe false
        isA(String::class.java).test("Alice") shouldBe true
        isA(CharSequence::class.java).test("Alice") shouldBe true

        isA(Integer::class.java).test(Integer.valueOf(42)) shouldBe true
        isA(Number::class.java).test(Integer.valueOf(42)) shouldBe true
    }

    "isSame() should check if obj is the same instance" {
        val p = Person("Alice", "Cooper", 74)
        isSame(p).test(Person("Alice", "Cooper", 74)) shouldBe false
        isSame(p).test(p) shouldBe true
        isSame(p).test(null) shouldBe false
        isSame(null).test(p) shouldBe false
        isSame(null).test(null) shouldBe true
    }

    "hasToString() should check if obj has the same toString() result" {
        val p = Person("Alice", "Cooper", 74)
        hasToString("Person(firstName=Alice, lastName=Cooper, age=74)").test(p) shouldBe true
        hasToString("waffles").test(p) shouldBe false
        hasToString("waffles").test(null) shouldBe false
        hasToString("null").test(null) shouldBe true
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

    "gt() should test if the original object is bigger than the given value" {
        gt(5).test(10) shouldBe true
        gt(5).test(5) shouldBe false
        gt(5).test(0) shouldBe false
        gt(5).test("Bob") shouldBe false
        gt(5).test(null) shouldBe false
    }

    "ge() should test if the original object is bigger than or equal to the given value" {
        ge(5).test(10) shouldBe true
        ge(5).test(5) shouldBe true
        ge(5).test(0) shouldBe false
        ge(5).test("Bob") shouldBe false
        ge(5).test(null) shouldBe false
    }

    "lt() should test if the original object is less than the given value" {
        lt(5).test(10) shouldBe false
        lt(5).test(5) shouldBe false
        lt(5).test(0) shouldBe true
        lt(5).test("Bob") shouldBe false
        lt(5).test(null) shouldBe false
    }

    "le() should test if the original object is less than the given value" {
        le(5).test(10) shouldBe false
        le(5).test(5) shouldBe true
        le(5).test(0) shouldBe true
        le(5).test("Bob") shouldBe false
        le(5).test(null) shouldBe false
    }
})
