package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.Serializable

class ConstantPatternTest : StringSpec({

    "'any' should match everything" {
        any(23) shouldBe true
        any(null) shouldBe true
    }

    "'none' shouldn't match anything" {
        none(23) shouldBe false
        none(null) shouldBe false
    }

    "'any()' should match everything" {
        any<Int?>()(23) shouldBe true
        any<Int?>()(null) shouldBe true
    }

    "'none()' shouldn't match anything" {
        none<Int?>()(23) shouldBe false
        none<Int?>()(null) shouldBe false
    }
})