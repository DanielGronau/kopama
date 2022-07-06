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
            else -> 4
        } shouldBe 3
    }

    "test" {
        listOf(1, 2, 3).component1()
    }
})
