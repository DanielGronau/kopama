package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.dslMatcher.match

class DslMatchingTest : StringSpec({

    "data class should be destructable" {
        val p = Person("Alice", "Cooper", 74)
        match(p) {
            String::class("Alice", "Cooper", 74) then { 1 }
            Person::class(startsWith("Al"), "Cooper", 7) then { 2 }
            Person::class("Alice", "Cooper", 74) then { 3 }
            Person::class("Alice", "Cooper", 74) then { 4 }
            otherwise { 5 }
        } shouldBe 3
    }

    "capture() should capture a value" {
        val p = Person("Alice", "Cooper", 74)
        match(p) {
            String::class("Alice", "Cooper", 74) then { 1 }
            val age = capture<Int>()
            Person::class(startsWith("B"), "Cooper", age) then { 2 }
            Person::class("Alice", "Cooper",  age) then { age.value }
            otherwise { 4 }
        } shouldBe 74
    }

    "on() should infer type" {
        val p = Person("Alice", "Cooper", 74)
        match(p) {
            eq(5) on { person: Person -> person.firstName.length } then { 1 }
            eq(5).on<Person> { it.firstName.length } then { 2 }
            startsWith("Al") on Person::firstName then { 3 }
            otherwise { 4 }
        } shouldBe 1
    }

})
