package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MatcherTest : StringSpec({

    "a matcher should return the lambda result of the first matching case" {
        match(23) {
            gt(25) then { "greater than 25" }
            gt(19) then { "greater than 19" }
            gt(11) then { "greater than 11" }
            otherwise { "no result" }
        } shouldBe "greater than 19"
    }

    "a matcher should return the 'otherwise' lambda result if no case matches" {
        match(23) {
            gt(25) then { "greater than 25" }
            lt(19) then { "less than 19" }
            lt(11) then { "less than 11" }
            otherwise { "no result" }
        } shouldBe "no result"
    }

    "a matcher should never evaluate lambdas of non-matching cases" {
        match(23) {
            gt(25) then { error("shouldn't be evaluated") }
            gt(19) then { "greater than 19" }
            gt(11) then { error("shouldn't be evaluated") }
            otherwise { error("shouldn't be evaluated") }
        } shouldBe "greater than 19"
    }

    "a matcher should allow to capture values" {
        match(23) {
            val capInt = capture<Int>()
            gt(25) and capInt then { "greater than 25, was ${capInt.value}" }
            gt(19) and capInt then { "greater than 19, was ${capInt.value}" }
            gt(11) and capInt then { "greater than 11, was ${capInt.value}" }
            otherwise { "no result" }
        } shouldBe "greater than 19, was 23"
    }

    "a matcher should overwrite '+' as a shortcut for 'eq'" {
        match("23") {
            +"11" then { "it's 11" }
            +"17" or +"19" then { "it's 17 or 19" }
            +"23" then { "it's 23" }
            +"42" then { "it's 42" }
            otherwise { "no result" }
        } shouldBe "it's 23"
    }

})