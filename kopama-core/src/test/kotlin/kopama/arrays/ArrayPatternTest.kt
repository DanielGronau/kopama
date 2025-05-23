package kopama.arrays

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.compare.eq
import kopama.compare.isNull
import kopama.compare.oneOf

class ArrayPatternTest : StringSpec({

    "'arrayIsEmpty' should check if an array is empty" {
        arrayIsEmpty()(arrayOf<Int>()) shouldBe true
        arrayIsEmpty()(arrayOf(1, 2, 3)) shouldBe false
    }

    "'arrayIsNotEmpty' should check if an array isn't empty" {
        arrayIsNotEmpty()(arrayOf(1, 2, 3)) shouldBe true
        arrayIsNotEmpty()(arrayOf<Int>()) shouldBe false
    }

    "'arrayHasSize' should check if an array has the given size" {
        arrayHasSize(0)(arrayOf<Int>()) shouldBe true
        arrayHasSize(2)(arrayOf<Int>()) shouldBe false
        arrayHasSize(2)(arrayOf(1)) shouldBe false
        arrayHasSize(2)(arrayOf(1, 2)) shouldBe true
        arrayHasSize(0)(arrayOf(1, 2)) shouldBe false
    }

    "'arrayHasSize' should check if an array matches the given pattern for its size" {
        arrayHasSize(oneOf(0, 2, 10))(arrayOf<Int>()) shouldBe true
        arrayHasSize(oneOf(2, 3))(arrayOf<Int>()) shouldBe false
        arrayHasSize(oneOf(2, 4, 6))(arrayOf(1)) shouldBe false
        arrayHasSize(oneOf(0, 2))(arrayOf(1, 2)) shouldBe true
        arrayHasSize(oneOf(0, 1))(arrayOf(1, 2)) shouldBe false
    }

    "'arrayForAll' should check if a pattern matches for all elements" {
        arrayForAll(isNull)(arrayOf()) shouldBe true
        arrayForAll(isNull)(arrayOf(null)) shouldBe true
        arrayForAll(isNull)(arrayOf(null, null)) shouldBe true
        arrayForAll(isNull)(arrayOf(1)) shouldBe false
        arrayForAll(isNull)(arrayOf(null, 1, null)) shouldBe false
    }

    "'arrayForAny' should check if a pattern matches for at least one element" {
        arrayForAny(isNull)(arrayOf()) shouldBe false
        arrayForAny(isNull)(arrayOf(null)) shouldBe true
        arrayForAny(isNull)(arrayOf(null, null)) shouldBe true
        arrayForAny(isNull)(arrayOf(1)) shouldBe false
        arrayForAny(isNull)(arrayOf(null, 1, null)) shouldBe true
        arrayForAny(isNull)(arrayOf(1, 2, 3)) shouldBe false
    }

    "'arrayForNone' should fail if a pattern matches for any element" {
        arrayForNone(isNull)(arrayOf()) shouldBe true
        arrayForNone(isNull)(arrayOf(null)) shouldBe false
        arrayForNone(isNull)(arrayOf(null, null)) shouldBe false
        arrayForNone(isNull)(arrayOf(1)) shouldBe true
        arrayForNone(isNull)(arrayOf(null, 1, null)) shouldBe false
        arrayForNone(isNull)(arrayOf(1, 2, 3)) shouldBe true
    }

    "'arrayContains' should check if an array contains a given value" {
        arrayContains("d")(arrayOf()) shouldBe false
        arrayContains("d")(arrayOf("a")) shouldBe false
        arrayContains("d")(arrayOf("d")) shouldBe true
        arrayContains("d")(arrayOf("d", "d")) shouldBe true
        arrayContains("d")(arrayOf("a", "b", "c", "d")) shouldBe true
        arrayContains("d")(arrayOf("a", "b", "c", "e")) shouldBe false
    }

    "'arrayContainsAll' should check if an array contains all given values" {
        arrayContainsAll("a", "b")(arrayOf()) shouldBe false
        arrayContainsAll("a", "b")(arrayOf("a")) shouldBe false
        arrayContainsAll("a", "b")(arrayOf("a", "c", "d")) shouldBe false
        arrayContainsAll("a", "b")(arrayOf("b", "c", "a")) shouldBe true
    }

    "'arrayContainsAny' should check if an array contains any of the given values" {
        arrayContainsAny("a", "b")(arrayOf()) shouldBe false
        arrayContainsAny("a", "b")(arrayOf("a")) shouldBe true
        arrayContainsAny("a", "b")(arrayOf("b", "c", "d")) shouldBe true
        arrayContainsAny("a", "b")(arrayOf("c", "d", "e")) shouldBe false
    }

    "'arrayContainsNone' should check if an array collection contains any of the given values" {
        arrayContainsNone("a", "b")(arrayOf()) shouldBe true
        arrayContainsNone("a", "b")(arrayOf("a")) shouldBe false
        arrayContainsNone("a", "b")(arrayOf("b", "c", "d")) shouldBe false
        arrayContainsNone("a", "b")(arrayOf("c", "d", "e")) shouldBe true
    }

    "`atIndex` should apply the given pattern at the indicated element" {
        (eq(5) atIndex 2)(arrayOf(1, 3, 5, 7)) shouldBe true
        (eq(5) atIndex 2)(arrayOf(5, 5, 6, 5)) shouldBe false
        (eq(5) atIndex 0)(arrayOf(5, 4, 2, 1)) shouldBe true
        (eq(5) atIndex 3)(arrayOf(1, 2, 4, 5)) shouldBe true
        (eq(5) atIndex 2)(arrayOf(1)) shouldBe false
        (eq(5) atIndex -2)(arrayOf(1)) shouldBe false
    }
})