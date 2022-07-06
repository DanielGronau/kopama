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
        contains(3).test(arrayOf(2,3,4)) shouldBe true
        contains(6).test(arrayOf(2,3,4)) shouldBe false
        contains(3).test(arrayOf("cat")) shouldBe false
        contains(3).test(arrayOf<Int>()) shouldBe false

        contains(3).test(listOf(2,3,4)) shouldBe true
        contains(6).test(listOf(2,3,4)) shouldBe false
        contains(3).test(listOf("cat")) shouldBe false
        contains(3).test(listOf<Int>()) shouldBe false

        contains(3).test(setOf(2,3,4)) shouldBe true
        contains(6).test(setOf(2,3,4)) shouldBe false
        contains(3).test(setOf("cat")) shouldBe false
        contains(3).test(setOf<Int>()) shouldBe false

        contains(3).test(sequenceOf(2,3,4)) shouldBe true
        contains(6).test(sequenceOf(2,3,4)) shouldBe false
        contains(3).test(sequenceOf("cat")) shouldBe false
        contains(3).test(sequenceOf<Int>()) shouldBe false
    }

    "contains() for maps should check if it contains the key" {
        contains(3).test(mapOf(2 to "two",3 to "three", 4 to "four")) shouldBe true
        contains(6).test(mapOf(2 to "two",3 to "three", 4 to "four")) shouldBe false
        contains(2).test(mapOf("two" to 2)) shouldBe false
    }

})
