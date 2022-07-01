package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MatchingTest : StringSpec({
    "data class should be destructable" {
        val p = Person("Alice", "Cooper", 74)
        when (match(p)) {
            "Human"(eq("Alice"), eq("Cooper"), eq(74)) -> 1
            "Person"(eq("Alice"), eq("Cooper"), eq(7)) -> 2
            "Person"(eq("Alice"), eq("Cooper"), eq(74)) -> 3
            else -> 4
        } shouldBe 3
    }
})