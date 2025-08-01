package kopama.example

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.compare.eq
import kopama.match

class ArgumentTest : StringSpec({

    "works for constructor var and vals, properties and methods" {
        match(ArgTest(11, listOf("2"))) {
            argTest(eq(1), baz = eq(12.1)) then { "nope" }
            argTest(eq(11), baz = eq(12.1), quux = +"346") then { "nope again" }
            argTest(eq(11), baz = eq(12.5), quux = +"345") then { "also nope" }
            argTest(eq(11), baz = eq(12.1), quux = +"345") then { "yupp" }
            otherwise { "oops" }
        } shouldBe "yupp"
    }

})