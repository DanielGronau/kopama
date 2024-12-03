package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TuplePatternTest : StringSpec({

    "'pair' should check if the given condition for the elements of a pair holds" {
        pair { a: Int, b: String -> a.toString() == b }(5 to "5") shouldBe true
        pair { a: Int, b: String -> a.toString() == b }(5 to "6") shouldBe false
    }

    "'pair' should check if the given patterns for the elements of a pair hold" {
        pair(startsWith("a"), gt(5))("and" to 7) shouldBe true
        pair(startsWith("a"), gt(5))("or" to 7) shouldBe false
        pair(startsWith("a"), gt(5))("and" to 3) shouldBe false
        pair(startsWith("a"), gt(5))("or" to 3) shouldBe false
    }

    "'pair_' should check if the given patterns for the elements of a pair hold" {
        match("and" to 7) {
            pair_(startsWith("b"), lt(5)) then { "wrong" }
            pair_(startsWith("a"), lt(5)) then { "wronger" }
            pair_(startsWith("b"), gt(5)) then { "wrongest" }
            pair_(startsWith("a"), gt(5)) then { "good" }
            otherwise { "oops" }
        } shouldBe "good"
    }

    "'first' should check if the given pattern holds for the first element of a pair" {
        first<String, Int>(startsWith("a"))("and" to 7) shouldBe true
        first<String, Int>(startsWith("a"))("or" to 7) shouldBe false
    }

    "'second' should check if the given pattern holds for the second element of a pair" {
        second<String, Int>(gt(5))("and" to 7) shouldBe true
        second<String, Int>(gt(5))("and" to 3) shouldBe false
    }

    "'triple' should check if the given condition for the elements of a triple holds" {
        triple { a: Int, b: String, c: Int -> a.toString() == b && c > a }(Triple(5, "5", 7)) shouldBe true
        triple { a: Int, b: String, c: Int -> a.toString() == b && c > a }(Triple(5, "6", 7)) shouldBe false
    }

    "'triple' should check if the given patterns for the elements of a triple hold" {
        triple(startsWith("a"), gt(5), lt(10))(Triple("and", 7, 5)) shouldBe true
        triple(startsWith("a"), gt(5), lt(10))(Triple("or", 7, 5)) shouldBe false
        triple(startsWith("a"), gt(5), lt(10))(Triple("and", 3, 5)) shouldBe false
        triple(startsWith("a"), gt(5), lt(10))(Triple("and", 7, 13)) shouldBe false
        triple(startsWith("a"), gt(5), lt(10))(Triple("or", 3, 13)) shouldBe false
    }

    "'triple_' should check if the given patterns for the elements of a triple hold" {
        match(Triple("and", 7, 5)) {
            triple(startsWith("a"), gt(5), gt(10)) then { "wrong1" }
            triple(startsWith("a"), lt(5), lt(10)) then { "wrong2" }
            triple(startsWith("b"), gt(5), lt(10)) then { "wrong3" }
            triple(startsWith("c"), lt(5), gt(10)) then { "wrong4" }
            triple(startsWith("a"), gt(5), lt(10)) then { "good" }
            otherwise { "oops" }
        } shouldBe "good"
    }

    "'triple1' should check if the given pattern holds for the first element of a triple" {
        triple1<String, Int, Int>(startsWith("a"))(Triple("and", 7, 9)) shouldBe true
        triple1<String, Int, Int>(startsWith("a"))(Triple("or", 7, 9)) shouldBe false
    }

    "'triple2' should check if the given pattern holds for the second element of a triple" {
        triple2<Int, String, Int>(startsWith("a"))(Triple(1, "and", 9)) shouldBe true
        triple2<Int, String, Int>(startsWith("a"))(Triple(1, "or", 9)) shouldBe false
    }

    "'triple' should check if the given pattern holds for the third element of a triple" {
        triple3<Int, Int, String>(startsWith("a"))(Triple(1, 2, "and")) shouldBe true
        triple3<Int, Int, String>(startsWith("a"))(Triple(1, 3, "or")) shouldBe false
    }
})