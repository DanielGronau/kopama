package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CollectionPatternTest : StringSpec({

    "'isEmpty' should check if a collection is empty" {
        isEmpty()(emptyList<Int>()) shouldBe true
        isEmpty()(listOf(1, 2, 3)) shouldBe false
    }

    "'isNotEmpty' should check if a collection isn't empty" {
        isNotEmpty()(listOf(1, 2, 3)) shouldBe true
        isNotEmpty()(emptyList<Int>()) shouldBe false
    }

    "'hasSize' should check if a collection has the given size" {
        hasSize(0)(emptyList<Int>()) shouldBe true
        hasSize(2)(emptyList<Int>()) shouldBe false
        hasSize(2)(listOf(1)) shouldBe false
        hasSize(2)(listOf(1, 2)) shouldBe true
        hasSize(0)(listOf(1, 2)) shouldBe false
    }

    "'forAll' should check if a pattern matches for all elements" {
        forAll(isNull)(listOf()) shouldBe true
        forAll(isNull)(listOf(null)) shouldBe true
        forAll(isNull)(listOf(null, null)) shouldBe true
        forAll(isNull)(listOf(1)) shouldBe false
        forAll(isNull)(listOf(null, 1, null)) shouldBe false
    }

    "'forAny' should check if a pattern matches for at least one element" {
        forAny(isNull)(listOf()) shouldBe false
        forAny(isNull)(listOf(null)) shouldBe true
        forAny(isNull)(listOf(null, null)) shouldBe true
        forAny(isNull)(listOf(1)) shouldBe false
        forAny(isNull)(listOf(null, 1, null)) shouldBe true
        forAny(isNull)(listOf(1, 2, 3)) shouldBe false
    }

    "'forNone' should fail if a pattern matches for any element" {
        forNone(isNull)(listOf()) shouldBe true
        forNone(isNull)(listOf(null)) shouldBe false
        forNone(isNull)(listOf(null, null)) shouldBe false
        forNone(isNull)(listOf(1)) shouldBe true
        forNone(isNull)(listOf(null, 1, null)) shouldBe false
        forNone(isNull)(listOf(1, 2, 3)) shouldBe true
    }

    "'contains' should check if a collection contains a given value" {
        contains("d")(listOf()) shouldBe false
        contains("d")(listOf("a")) shouldBe false
        contains("d")(listOf("d")) shouldBe true
        contains("d")(listOf("d", "d")) shouldBe true
        contains("d")(listOf("a", "b", "c", "d")) shouldBe true
        contains("d")(listOf("a", "b", "c", "e")) shouldBe false
    }

    "'containsAll' should check if a collection contains all given values" {
        containsAll("a", "b")(listOf()) shouldBe false
        containsAll("a", "b")(listOf("a")) shouldBe false
        containsAll("a", "b")(listOf("a", "c", "d")) shouldBe false
        containsAll("a", "b")(listOf("b", "c", "a")) shouldBe true
    }

    "'containsAny' should check if a collection contains any of the given values" {
        containsAny("a", "b")(listOf()) shouldBe false
        containsAny("a", "b")(listOf("a")) shouldBe true
        containsAny("a", "b")(listOf("b", "c", "d")) shouldBe true
        containsAny("a", "b")(listOf("c", "d", "e")) shouldBe false
    }

    "'containsNone' should check if a collection contains any of the given values" {
        containsNone("a", "b")(listOf()) shouldBe true
        containsNone("a", "b")(listOf("a")) shouldBe false
        containsNone("a", "b")(listOf("b", "c", "d")) shouldBe false
        containsNone("a", "b")(listOf("c", "d", "e")) shouldBe true
    }

    "the indexed access should apply the given pattern at the indicated element" {
        eq(5)[2](listOf(1, 3, 5, 7)) shouldBe true
        eq(5)[2](listOf(5, 5, 6, 5)) shouldBe false
        eq(5)[0](listOf(5, 4, 2, 1)) shouldBe true
        eq(5)[3](listOf(1, 2, 4, 5)) shouldBe true
        eq(5)[2](listOf(1)) shouldBe false
        eq(5)[-2](listOf(1)) shouldBe false
    }
})