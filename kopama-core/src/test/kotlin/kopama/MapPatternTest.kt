package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.Serializable

class MapPatternTest : StringSpec({

    "'keys' should check if the given pattern matches the keys of a map" {
        keys<String, Int>(forAll(startsWith("a")))(mapOf("a" to 1, "ab" to 2)) shouldBe true
        keys<String, Int>(forAll(startsWith("a")))(mapOf("a" to 1, "b" to 2)) shouldBe false
    }

    "'values' should check if the given pattern matches the values of a map" {
        values<Int, String>(forAll(startsWith("a")))(mapOf(1 to "a", 2 to "ab")) shouldBe true
        values<Int, String>(forAll(startsWith("a")))(mapOf(1 to "a", 2 to "b")) shouldBe false
    }

    "'entries' should check if the given pattern matches the entries of a map" {
        entries(forAll(pair { a: Int, b: String -> a.toString() == b }))(mapOf(5 to "5", 7 to "7")) shouldBe true
        entries(forAll(pair { a: Int, b: String -> a.toString() == b }))(mapOf(5 to "5", 8 to "7")) shouldBe false
    }

    "'valueAt' should check if a value for a key matches the given pattern" {
        eq(5).valueAt("b")(mapOf("a" to 3, "b" to 5, "c" to 7)) shouldBe true
        eq(6).valueAt("b")(mapOf("a" to 3, "b" to 5, "c" to 7)) shouldBe false
        eq(5).valueAt("x")(mapOf("a" to 3, "b" to 5, "c" to 7)) shouldBe false
    }
})