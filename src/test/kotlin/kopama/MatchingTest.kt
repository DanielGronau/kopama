package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MatchingTest : StringSpec({

    "data class should be destructable" {
        val p = Person("Alice", "Cooper", 74)
        when (match(p)) {
            String::class("Alice", "Cooper", 74) -> 1
            Person::class(startsWith("Al"), "Cooper", 7) -> 2
            Person::class("Alice", "Cooper", 74) -> 3
            Person::class("Alice", "Cooper", 74) -> 4
            else -> 5
        } shouldBe 3
    }

    "capture() should capture a value" {
        val p = Person("Alice", "Cooper", 74)
        val c = capture<Int>()
        c.test("x") shouldBe false
        when (match(p)) {
            String::class("Alice", "Cooper", 74) -> 1
            Person::class(startsWith("B"), "Cooper", c) -> 2
            Person::class("Alice", "Cooper", c) -> c.value
            else -> 4
        } shouldBe 74
    }

    "on() should infer type" {
        val p = Person("Alice", "Cooper", 74)
        when(match(p)) {
            eq(5) on { person:Person -> person.firstName.length } -> 1
            eq(5).on<Person>{ it.firstName.length } -> 2
            startsWith("Al") on Person::firstName -> 3
            else -> 4
        } shouldBe 1
    }

})
