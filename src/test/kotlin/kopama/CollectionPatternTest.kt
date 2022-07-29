package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CollectionPatternTest : StringSpec({

    "size() should check the size or length of the object" {
        size(2).test(arrayOf(1, 2)) shouldBe true
        size(3).test(arrayOf(1, 2)) shouldBe false
        size(2).test(listOf(1, 2)) shouldBe true
        size(3).test(listOf(1, 2)) shouldBe false
        size(2).test(setOf(1, 2)) shouldBe true
        size(3).test(setOf(1, 2)) shouldBe false
        size(2).test(sequenceOf(1, 2)) shouldBe true
        size(3).test(sequenceOf(1, 2)) shouldBe false
        size(2).test(mapOf(1 to "one", 2 to "two")) shouldBe true
        size(3).test(mapOf(1 to "one", 2 to "two")) shouldBe false
        size(2).test("np") shouldBe true
        size(3).test("np") shouldBe false
        size(3).test(null) shouldBe false
        size(3).test(42) shouldBe false
    }

    "empty should check whether the object (e.g. collection) is empty" {
        empty.test(arrayOf<String>()) shouldBe true
        empty.test(arrayOf(1, 2)) shouldBe false
        empty.test(listOf<String>()) shouldBe true
        empty.test(listOf(1, 2)) shouldBe false
        empty.test(setOf<String>()) shouldBe true
        empty.test(setOf(1, 2)) shouldBe false
        empty.test(sequenceOf<String>()) shouldBe true
        empty.test(sequenceOf(1, 2)) shouldBe false
        empty.test(mapOf<Int, String>()) shouldBe true
        empty.test(mapOf(1 to "one", 2 to "two")) shouldBe false
        empty.test("") shouldBe true
        empty.test("np") shouldBe false
        empty.test(null) shouldBe false
        empty.test(42) shouldBe false
    }

    "contains for CharSequences should test whether it contains the given object" {
        contains("cat").test("caterpillar") shouldBe true
        contains("cat").test("bobcat") shouldBe true
        contains("cat").test("scatter") shouldBe true
        contains("cat").test("cart") shouldBe false

        contains('a').test("cat") shouldBe true
        contains('b').test("cat") shouldBe false

        contains(Regex("[a-h]at")).test("cat") shouldBe true
        contains(Regex("[i-z]at")).test("cat") shouldBe false

        contains("cat").test(StringBuilder("caterpillar")) shouldBe true
        contains("cat").test(StringBuilder("bobcat")) shouldBe true
        contains("cat").test(StringBuilder("scatter")) shouldBe true
        contains("cat").test(StringBuilder("cart")) shouldBe false

        contains('a').test(StringBuilder("cat")) shouldBe true
        contains('b').test(StringBuilder("cat")) shouldBe false

        contains(Regex("[a-h]at")).test(StringBuilder("cat")) shouldBe true
        contains(Regex("[i-z]at")).test(StringBuilder("cat")) shouldBe false

        contains("cat").test(null) shouldBe false
        contains("cat").test(42) shouldBe false
    }

    "contains() for arrays, iterables, sequences should check if it contains the element" {
        contains(3).test(arrayOf(2, 3, 4)) shouldBe true
        contains(6).test(arrayOf(2, 3, 4)) shouldBe false
        contains(3).test(arrayOf("cat")) shouldBe false
        contains(3).test(arrayOf<Int>()) shouldBe false

        contains(3).test(listOf(2, 3, 4)) shouldBe true
        contains(6).test(listOf(2, 3, 4)) shouldBe false
        contains(3).test(listOf("cat")) shouldBe false
        contains(3).test(listOf<Int>()) shouldBe false

        contains(3).test(setOf(2, 3, 4)) shouldBe true
        contains(6).test(setOf(2, 3, 4)) shouldBe false
        contains(3).test(setOf("cat")) shouldBe false
        contains(3).test(setOf<Int>()) shouldBe false

        contains(3).test(sequenceOf(2, 3, 4)) shouldBe true
        contains(6).test(sequenceOf(2, 3, 4)) shouldBe false
        contains(3).test(sequenceOf("cat")) shouldBe false
        contains(3).test(sequenceOf<Int>()) shouldBe false
    }

    "contains() for maps should check if it contains the key" {
        contains(3).test(mapOf(2 to "two", 3 to "three", 4 to "four")) shouldBe true
        contains(6).test(mapOf(2 to "two", 3 to "three", 4 to "four")) shouldBe false
        contains(2).test(mapOf("two" to 2)) shouldBe false
    }

    "all() should check if all elements match the given pattern" {
        all(gt(5)).test(arrayOf<Int>()) shouldBe true
        all(gt(5)).test(arrayOf(1, 2, 6, 7)) shouldBe false
        all(gt(5)).test(arrayOf(9, 8, 6, 7)) shouldBe true
        all(gt(5)).test(arrayOf(9, null, 6, 7)) shouldBe false
        all(gt(5)).test(arrayOf("foo", "bar", 7)) shouldBe false

        all(gt(5)).test(listOf<Int>()) shouldBe true
        all(gt(5)).test(listOf(1, 2, 6, 7)) shouldBe false
        all(gt(5)).test(listOf(9, 8, 6, 7)) shouldBe true
        all(gt(5)).test(listOf(9, null, 6, 7)) shouldBe false
        all(gt(5)).test(listOf("foo", "bar", 7)) shouldBe false

        all(gt(5)).test(setOf<Int>()) shouldBe true
        all(gt(5)).test(setOf(1, 2, 6, 7)) shouldBe false
        all(gt(5)).test(setOf(9, 8, 6, 7)) shouldBe true
        all(gt(5)).test(setOf(9, null, 6, 7)) shouldBe false
        all(gt(5)).test(setOf("foo", "bar", 7)) shouldBe false

        all(gt(5)).test(sequenceOf<Int>()) shouldBe true
        all(gt(5)).test(sequenceOf(1, 2, 6, 7)) shouldBe false
        all(gt(5)).test(sequenceOf(9, 8, 6, 7)) shouldBe true
        all(gt(5)).test(sequenceOf(9, null, 6, 7)) shouldBe false
        all(gt(5)).test(sequenceOf("foo", "bar", 7)) shouldBe false

        all(gt('b')).test("") shouldBe true
        all(gt('b')).test("cat") shouldBe false
        all(gt('b')).test("mouse") shouldBe true
    }

    "exists() should check if any elements match the given pattern" {
        exists(gt(5)).test(arrayOf<Int>()) shouldBe false
        exists(gt(5)).test(arrayOf(1, 2, 3, 5)) shouldBe false
        exists(gt(5)).test(arrayOf(1, 8, 0, 2)) shouldBe true
        exists(gt(5)).test(arrayOf(9, null, 1, 2)) shouldBe true
        exists(gt(5)).test(arrayOf("foo", "bar", 7)) shouldBe true
        exists(gt(5)).test(arrayOf("foo", "bar", null)) shouldBe false

        exists(gt(5)).test(listOf<Int>()) shouldBe false
        exists(gt(5)).test(listOf(1, 2, 3, 5)) shouldBe false
        exists(gt(5)).test(listOf(1, 8, 0, 2)) shouldBe true
        exists(gt(5)).test(listOf(9, null, 1, 2)) shouldBe true
        exists(gt(5)).test(listOf("foo", "bar", 7)) shouldBe true
        exists(gt(5)).test(listOf("foo", "bar", null)) shouldBe false

        exists(gt(5)).test(setOf<Int>()) shouldBe false
        exists(gt(5)).test(setOf(1, 2, 3, 5)) shouldBe false
        exists(gt(5)).test(setOf(1, 8, 0, 2)) shouldBe true
        exists(gt(5)).test(setOf(9, null, 1, 2)) shouldBe true
        exists(gt(5)).test(setOf("foo", "bar", 7)) shouldBe true
        exists(gt(5)).test(setOf("foo", "bar", null)) shouldBe false

        exists(gt(5)).test(sequenceOf<Int>()) shouldBe false
        exists(gt(5)).test(sequenceOf(1, 2, 3, 5)) shouldBe false
        exists(gt(5)).test(sequenceOf(1, 8, 0, 2)) shouldBe true
        exists(gt(5)).test(sequenceOf(9, null, 1, 2)) shouldBe true
        exists(gt(5)).test(sequenceOf("foo", "bar", 7)) shouldBe true
        exists(gt(5)).test(sequenceOf("foo", "bar", null)) shouldBe false

        exists(gt('t')).test("") shouldBe false
        exists(gt('t')).test("cat") shouldBe false
        exists(gt('t')).test("mouse") shouldBe true
    }

    "none() should check if no elements match the given pattern" {
        none(gt(5)).test(arrayOf<Int>()) shouldBe true
        none(gt(5)).test(arrayOf(1, 2, 3, 5)) shouldBe true
        none(gt(5)).test(arrayOf(1, 8, 0, 2)) shouldBe false
        none(gt(5)).test(arrayOf(9, null, 1, 2)) shouldBe false
        none(gt(5)).test(arrayOf("foo", "bar", 7)) shouldBe false
        none(gt(5)).test(arrayOf("foo", "bar", null)) shouldBe true

        none(gt(5)).test(listOf<Int>()) shouldBe true
        none(gt(5)).test(listOf(1, 2, 3, 5)) shouldBe true
        none(gt(5)).test(listOf(1, 8, 0, 2)) shouldBe false
        none(gt(5)).test(listOf(9, null, 1, 2)) shouldBe false
        none(gt(5)).test(listOf("foo", "bar", 7)) shouldBe false
        none(gt(5)).test(listOf("foo", "bar", null)) shouldBe true

        none(gt(5)).test(setOf<Int>()) shouldBe true
        none(gt(5)).test(setOf(1, 2, 3, 5)) shouldBe true
        none(gt(5)).test(setOf(1, 8, 0, 2)) shouldBe false
        none(gt(5)).test(setOf(9, null, 1, 2)) shouldBe false
        none(gt(5)).test(setOf("foo", "bar", 7)) shouldBe false
        none(gt(5)).test(setOf("foo", "bar", null)) shouldBe true

        none(gt(5)).test(sequenceOf<Int>()) shouldBe true
        none(gt(5)).test(sequenceOf(1, 2, 3, 5)) shouldBe true
        none(gt(5)).test(sequenceOf(1, 8, 0, 2)) shouldBe false
        none(gt(5)).test(sequenceOf(9, null, 1, 2)) shouldBe false
        none(gt(5)).test(sequenceOf("foo", "bar", 7)) shouldBe false
        none(gt(5)).test(sequenceOf("foo", "bar", null)) shouldBe true

        none(gt('t')).test("") shouldBe true
        none(gt('t')).test("cat") shouldBe true
        none(gt('t')).test("mouse") shouldBe false
    }

    "allKeys() should check if all keys of a map match the given pattern" {
        allKeys(gt(5)).test(mapOf(6 to "x", 8 to "y")) shouldBe true
        allKeys(gt(5)).test(mapOf(6 to "x", 5 to "y")) shouldBe false
        allKeys(gt(5)).test(mapOf<Int, String>()) shouldBe true
        allKeys(gt(5)).test(mapOf("x" to 6, "y" to 5)) shouldBe false
        allKeys(gt(5)).test("nope") shouldBe false
        allKeys(gt(5)).test(null) shouldBe false
    }

    "existsKey() should check if any of the keys of a map matches the given pattern" {
        existsKey(gt(5)).test(mapOf(6 to "x", 3 to "y")) shouldBe true
        existsKey(gt(5)).test(mapOf(3 to "x", 5 to "y")) shouldBe false
        existsKey(gt(5)).test(mapOf<Int, String>()) shouldBe false
        existsKey(gt(5)).test(mapOf("x" to 6, "y" to 5)) shouldBe false
        existsKey(gt(5)).test("nope") shouldBe false
        existsKey(gt(5)).test(null) shouldBe false
    }

    "noKey() should check if none of the keys of a map matches the given pattern" {
        noKey(gt(5)).test(mapOf(6 to "x", 3 to "y")) shouldBe false
        noKey(gt(5)).test(mapOf(3 to "x", 5 to "y")) shouldBe true
        noKey(gt(5)).test(mapOf<Int, String>()) shouldBe true
        noKey(gt(5)).test(mapOf("x" to 6, "y" to 5)) shouldBe true
        noKey(gt(5)).test("nope") shouldBe false
        noKey(gt(5)).test(null) shouldBe false
    }

    "allValues() should check if all values of a map match the given pattern" {
        allValues(gt(5)).test(mapOf("x" to 6, "y" to 8)) shouldBe true
        allValues(gt(5)).test(mapOf("x" to 6, "y" to 5)) shouldBe false
        allValues(gt(5)).test(mapOf<String, Int>()) shouldBe true
        allValues(gt(5)).test(mapOf(6 to "x", 5 to "y")) shouldBe false
        allValues(gt(5)).test("nope") shouldBe false
        allValues(gt(5)).test(null) shouldBe false
    }

    "existsValue() should check if any value of a map matches the given pattern" {
        existsValue(gt(5)).test(mapOf("x" to 6, "y" to 4)) shouldBe true
        existsValue(gt(5)).test(mapOf("x" to 4, "y" to 5)) shouldBe false
        existsValue(gt(5)).test(mapOf<String, Int>()) shouldBe false
        existsValue(gt(5)).test(mapOf(6 to "x", 5 to "y")) shouldBe false
        existsValue(gt(5)).test("nope") shouldBe false
        existsValue(gt(5)).test(null) shouldBe false
    }

    "noValue() should check if no value of a map matches the given pattern" {
        noValue(gt(5)).test(mapOf("x" to 6, "y" to 4)) shouldBe false
        noValue(gt(5)).test(mapOf("x" to 4, "y" to 5)) shouldBe true
        noValue(gt(5)).test(mapOf<String, Int>()) shouldBe true
        noValue(gt(5)).test(mapOf(6 to "x", 5 to "y")) shouldBe true
        noValue(gt(5)).test("nope") shouldBe false
        noValue(gt(5)).test(null) shouldBe false
    }
})
