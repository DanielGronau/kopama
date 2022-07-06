package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SplitPatternTest : StringSpec({

    "splitting data classes should work" {
        val p = Person("Alice", "Cooper", 74)

        "Human"("Alice", "Cooper", 74).test(p) shouldBe false
        "Person"("Alice", "Cooper", 74).test(p) shouldBe true
        "kopama.Person"("Alice", "Cooper", 74).test(p) shouldBe true

        "Person"("Alice", "Cooper", 74, 0.0).test(p) shouldBe false
        "Person"("Alice", "Cooper").test(p) shouldBe true
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
        "ArrayList"(2).test(l) shouldBe true
        "ArrayList"(2, 1).test(l) shouldBe true
        "ArrayList"(2, 1, 3).test(l) shouldBe true
        "ArrayList"(2, 1, 3, 4).test(l) shouldBe true
        "ArrayList"(2, 1, 3, 4, 7).test(l) shouldBe false

        "ArrayList"("Alice").test(l) shouldBe false
        "ArrayList"(null).test(l) shouldBe false

        "List"(2, 1, 3, 4).test(l) shouldBe false
        "java.util.Arrays.ArrayList"(2, 1, 3, 4).test(l) shouldBe true
    }

    "anonymous splitting of data classes should work" {
        val p = Person("Alice", "Cooper", 74)

        split("Alice", "Cooper", 74).test(p) shouldBe true
        split("Alice", "Cooper", 74, 0.0).test(p) shouldBe false
        split("Alice", "Cooper").test(p) shouldBe true
    }

    "anonymous splitting of Iterables should work" {
        val l = listOf(2, 1, 3, 4)

        split().test(l) shouldBe true
        split(2).test(l) shouldBe true
        split(2, 1).test(l) shouldBe true
        split(2, 1, 3).test(l) shouldBe true
        split(2, 1, 3, 4).test(l) shouldBe true
        split(2, 1, 3, 4, 7).test(l) shouldBe false

        split("Alice").test(l) shouldBe false
        split(null).test(l) shouldBe false
    }

    "splitting should nest" {
        val l = listOf(
            Person("Alice", "Cooper", 74),
            Person("Mick", "Jagger", 78)
        )
        split(
            "Person"("Alice", any, 74),
            split(any, "Jagger", 78)
        ).test(l) shouldBe true
        split(
            Person("Alice", "Cooper", 74),
            split(any, "Jagger", 78)
        ).test(l) shouldBe true
        split(
            "Person"("Alice", any, 71),
            split(any, "Jagger", 78)
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

    "indexed access should work for char sequences" {
        eq('c')[3].test("Alice") shouldBe true
        eq('d')[3].test("Alice") shouldBe false
        eq('c')[-3].test("Alice") shouldBe false
        eq('c')[30].test("Alice") shouldBe false
        eq('c')[3].test(StringBuilder("Alice")) shouldBe true
        eq('d')[3].test(StringBuilder("Alice")) shouldBe false
        eq('c')[-3].test(StringBuilder("Alice")) shouldBe false
        eq('c')[30].test(StringBuilder("Alice")) shouldBe false
    }

    "named access should work" {
        val p = Person("Alice", "Cooper", 74)
        eq("Alice")["firstName"].test(p) shouldBe true
        eq("Person(firstName=Alice, lastName=Cooper, age=74)")["toString"].test(p) shouldBe true
    }

    "named access should work for nested properties" {
        val p = Person("Alice", "Cooper", 74)
        eq(5)["firstName.length"].test(p) shouldBe true
        eq(6)["firstName.length"].test(p) shouldBe false
        eq(48)["toString.length"].test(p) shouldBe true
        eq(0)["toString.length"].test(p) shouldBe false
    }

    "map access should work" {
        eq(42)["fortyTwo"].test(mapOf("one" to 1, "fortyTwo" to 42)) shouldBe true
        eq(42)["fortyTwo"].test(mapOf("one" to 1)) shouldBe false
        eq("fortyTwo")[42].test(mapOf(1 to "one", 42 to "fortyTwo")) shouldBe true
        eq("fortyTwo")[42].test(mapOf(1 to "one")) shouldBe false
        eq("fortyTwo")[42L].test(mapOf(1L to "one", 42L to "fortyTwo")) shouldBe true
        eq("fortyTwo")[42L].test(mapOf(1L to "one")) shouldBe false
        eq("fortyTwo")[42L].test(null) shouldBe false
    }
})
