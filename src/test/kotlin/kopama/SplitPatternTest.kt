package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SplitPatternTest : StringSpec({

    "splitting data classes should work" {
        val p = Person("Alice", "Cooper", 74)

        "Human"(eq("Alice"), eq("Cooper"), eq(74)).test(p) shouldBe false
        "Person"(eq("Alice"), eq("Cooper"), eq(74)).test(p) shouldBe true
        "kopama.Person"(eq("Alice"), eq("Cooper"), eq(74)).test(p) shouldBe true

        "Person"(eq("Alice"), eq("Cooper"), eq(74), eq(0.0)).test(p) shouldBe false
        "Person"(eq("Alice"), eq("Cooper")).test(p) shouldBe true
        "Person"().test(p) shouldBe true
    }

    "splitting normal classes should work" {
        "String"().test("foo") shouldBe true
        "kotlin.String"().test("foo") shouldBe true
        "Text"().test("foo") shouldBe false
    }

    "splitting Iterables should work" {
        val l = listOf(2, 1, 3, 4)

        "ArrayList"().test(l) shouldBe true
        "ArrayList"(eq(2)).test(l) shouldBe true
        "ArrayList"(eq(2), eq(1)).test(l) shouldBe true
        "ArrayList"(eq(2), eq(1), eq(3)).test(l) shouldBe true
        "ArrayList"(eq(2), eq(1), eq(3), eq(4)).test(l) shouldBe true
        "ArrayList"(eq(2), eq(1), eq(3), eq(4), eq(7)).test(l) shouldBe false

        "ArrayList"(eq("Alice")).test(l) shouldBe false
        "ArrayList"(eq(null)).test(l) shouldBe false

        "List"(eq(2), eq(1), eq(3), eq(4)).test(l) shouldBe false
        "java.util.Arrays.ArrayList"(eq(2), eq(1), eq(3), eq(4)).test(l) shouldBe true
    }

    "anonymous splitting of data classes should work" {
        val p = Person("Alice", "Cooper", 74)

        split(eq("Alice"), eq("Cooper"), eq(74)).test(p) shouldBe true
        split(eq("Alice"), eq("Cooper"), eq(74), eq(0.0)).test(p) shouldBe false
        split(eq("Alice"), eq("Cooper")).test(p) shouldBe true
    }

    "anonymous splitting of Iterables should work" {
        val l = listOf(2, 1, 3, 4)

        split().test(l) shouldBe true
        split(eq(2)).test(l) shouldBe true
        split(eq(2), eq(1)).test(l) shouldBe true
        split(eq(2), eq(1), eq(3)).test(l) shouldBe true
        split(eq(2), eq(1), eq(3), eq(4)).test(l) shouldBe true
        split(eq(2), eq(1), eq(3), eq(4), eq(7)).test(l) shouldBe false

        split(eq("Alice")).test(l) shouldBe false
        split(eq(null)).test(l) shouldBe false
    }

    "splitting should nest" {
        val l = listOf(
            Person("Alice", "Cooper", 74),
            Person("Mick", "Jagger", 78)
        )
        split(
            "Person"(eq("Alice"), any, eq(74)),
            split(any, eq("Jagger"), eq(78))
        ).test(l) shouldBe true
        split(
            "Person"(eq("Alice"), any, eq(71)),
            split(any, eq("Jagger"), eq(78))
        ).test(l) shouldBe false
    }

    "indexed access should work for arrays" {
        eq("Bob")[1].test(arrayOf("Alice", "Bob", "Charlie")) shouldBe true
        eq("Bob")[-1].test(arrayOf("Alice", "Bob", "Charlie")) shouldBe false
        eq("Bob")[2].test(arrayOf("Alice", "Bob", "Charlie")) shouldBe false
        eq("Bob")[10].test(arrayOf("Alice", "Bob", "Charlie")) shouldBe false
        isNull[1].test(arrayOf("Alice", null, "Charlie")) shouldBe true
        isNull[10].test(arrayOf("Alice", null, "Charlie")) shouldBe false
    }

    "indexed access should work for lists" {
        eq("Bob")[1].test(listOf("Alice", "Bob", "Charlie")) shouldBe true
        eq("Bob")[-1].test(listOf("Alice", "Bob", "Charlie")) shouldBe false
        eq("Bob")[2].test(listOf("Alice", "Bob", "Charlie")) shouldBe false
        eq("Bob")[10].test(listOf("Alice", "Bob", "Charlie")) shouldBe false
        isNull[1].test(listOf("Alice", null, "Charlie")) shouldBe true
        isNull[10].test(listOf("Alice", null, "Charlie")) shouldBe false
    }

    "indexed access should work for collections" {
        // note that setOf preserves order, as it uses LinkedHashSet
        eq("Bob")[1].test(setOf("Alice", "Bob", "Charlie")) shouldBe true
        eq("Bob")[-1].test(setOf("Alice", "Bob", "Charlie")) shouldBe false
        eq("Bob")[2].test(setOf("Alice", "Bob", "Charlie")) shouldBe false
        eq("Bob")[10].test(setOf("Alice", "Bob", "Charlie")) shouldBe false
        isNull[1].test(setOf("Alice", null, "Charlie")) shouldBe true
        isNull[10].test(setOf("Alice", null, "Charlie")) shouldBe false
    }

    "indexed access should work for sequences" {
        eq("Bob")[1].test(sequenceOf("Alice", "Bob", "Charlie")) shouldBe true
        eq("Bob")[-1].test(sequenceOf("Alice", "Bob", "Charlie")) shouldBe false
        eq("Bob")[2].test(sequenceOf("Alice", "Bob", "Charlie")) shouldBe false
        eq("Bob")[10].test(sequenceOf("Alice", "Bob", "Charlie")) shouldBe false
        isNull[1].test(sequenceOf("Alice", null, "Charlie")) shouldBe true
        isNull[10].test(sequenceOf("Alice", null, "Charlie")) shouldBe false
    }

    "indexed access should work for data classes" {
        val p = Person("Alice", "Cooper", 74)
        eq("Alice")[1].test(p) shouldBe true
        eq("Cooper")[2].test(p) shouldBe true
        eq(74)[3].test(p) shouldBe true
        eq("Bob")[-1].test(p) shouldBe false
        eq("Bob")[1].test(p) shouldBe false
        eq("Bob")[10].test(p) shouldBe false
    }

})
