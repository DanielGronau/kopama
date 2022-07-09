package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MatchingTest : StringSpec({

    "data class should be destructable" {
        val p = Person("Alice", "Cooper", 74)
        when (match(p)) {
            "Human"("Alice", "Cooper", 74) -> 1
            "Person"(startsWith("Al"), "Cooper", 7) -> 2
            "Person"("Alice", "Cooper", 74) -> 3
            "Person"("Alice", "Cooper", 74) -> 3
            else -> 4
        } shouldBe 3
    }

    "capture() should capture a value" {
        val p = Person("Alice", "Cooper", 74)
        val c = capture()
        c.value shouldBe ValueNotSet
        when (match(p)) {
            "Human"("Alice", "Cooper", 74) -> 1
            "Person"(startsWith("B"), "Cooper", c) -> 2
            "Person"("Alice", "Cooper", c) -> c.value
            else -> 4
        } shouldBe 74
    }

})
