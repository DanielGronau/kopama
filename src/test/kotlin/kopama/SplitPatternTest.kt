package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.util.*

class SplitPatternTest : StringSpec({

    "splitting data classes should work" {
        val p = Person("Alice", "Cooper", 74)

        String::class("Alice", "Cooper", 74).test(p) shouldBe false
        Person::class("Alice", "Cooper", 74).test(p) shouldBe true

        Person::class("Alice", "Cooper", 74, 0.0).test(p) shouldBe false
        Person::class("Alice", "Cooper").test(p) shouldBe true
        Person::class().test(p) shouldBe true
    }

    "splitting normal classes should work" {
        String::class().test("foo") shouldBe true
        Date::class().test("foo") shouldBe false
    }

    "splitting Iterables should work" {
        val l = listOf(2, 1, 3, 4)

        List::class().test(l) shouldBe true
        List::class(2).test(l) shouldBe true
        List::class(2, 1).test(l) shouldBe true
        List::class(2, 1, 3).test(l) shouldBe true
        List::class(2, 1, 3, 4).test(l) shouldBe true
        List::class(2, 1, 3, 4, 7).test(l) shouldBe false

        List::class("Alice").test(l) shouldBe false
        List::class(null).test(l) shouldBe false

        ArrayList::class(2, 1, 3, 4).test(l) shouldBe false
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
            Person::class("Alice", any, 74),
            split(any, "Jagger", 78)
        ).test(l) shouldBe true

        split(
            Person("Alice", "Cooper", 74),
            split(any, "Jagger", 78)
        ).test(l) shouldBe true

        split(
            Person::class("Alice", any, 71),
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

    "reflective property access should work" {
        val p = Person("Alice", "Cooper", 74)
        gt(70)[Person::age].test(p) shouldBe true
        lt(70)[Person::age].test(p) shouldBe false
        lt(70)[Person::age].test("oops") shouldBe false
        lt(70)[Person::age].test(null) shouldBe false
    }

    "reflective method access should work" {
        val p = Person("Alice", "Cooper", 74)
        startsWith("Person(")[Person::toString].test(p) shouldBe true
        startsWith("Alien(")[Person::toString].test(p) shouldBe false
        startsWith("Person(")[Person::toString].test(42) shouldBe false
        startsWith("Person(")[Person::toString].test(null) shouldBe false
    }

    "nested reflective access should work" {
        val p = Person("Alice", "Cooper", 74)
        eq(5)[String::length][Person::firstName].test(p) shouldBe true
        eq(6)[String::length][Person::firstName].test(p) shouldBe false
        eq(6)[String::length][Person::firstName].test("oops") shouldBe false
        eq(6)[String::length][Person::firstName].test(null) shouldBe false
    }

    "on operator should work" {
        val p = Person("Alice", "Cooper", 74)
        (eq(5) on { person: Person -> person.firstName.length }).test(p) shouldBe true
        (eq(6) on { person: Person -> person.firstName.length }).test(p) shouldBe false
        (eq(5).on<Person> { it.firstName.length }).test(p) shouldBe true
        (eq(6).on<Person> { it.firstName.length }).test(p) shouldBe false
        (eq(6).on<Person> { it.firstName.length }).test("oops") shouldBe false
        (eq(6).on<Person> { it.firstName.length }).test(null) shouldBe false
    }
})
