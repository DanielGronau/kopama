package kopama.example

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.Invalid
import kopama.compare.eq
import kopama.compare.ge
import kopama.compare.isNotNull
import kopama.compare.isNull
import kopama.strings.startsWith
import kopama.validate

class ValidatorTest : StringSpec({

    "validator works for generated classes" {
        validate(NullTest(12, null, "foo", "bar")) {
            nullTest(alpha = ge(20)) onFail { "alpha too small" } // matches
            nullTest(beta = isNotNull) onFail { "beta is null" } // matches
            nullTest(gamma = startsWith("fo")) onFail { "gamma doesn't start with foo" }
            nullTest(delta = isNull) onFail { "delta is not null" } // matches
            nullTest(eq(12), isNull, +"foo", +"bar") onFail { "oops" }
        } shouldBe Invalid("alpha too small", "beta is null", "delta is not null")
    }

})