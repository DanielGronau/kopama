package kopama.dataclasses

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.compare.eq
import kopama.compare.isNull
import kopama.match
import java.lang.IllegalArgumentException

class DataClassPatternTest : StringSpec({

    data class Test3(val foo: String, var bar: Int, val baz: Int?)

    val test3 = DataClassPattern3<Test3, String, Int, Int?>()

    "data class patterns should match correctly" {
        match(Test3("quux", 42, null)) {
            test3(+"quux", eq(12), isNull) then { "wrong" }
            test3(+"quux", comp3 = eq(12)) then { "wronger" }
            test3(comp3 = eq(1)) then { "wrongest" }
            test3(+"quux", eq(42), isNull) then { "yupp" }
            otherwise { "unknown" }
        } shouldBe "yupp"
    }

    "non-data classes must not be instantiatable" {
        shouldThrow< IllegalArgumentException> {
            DataClassPattern2<String, Int, String>()
        }.message shouldBe "'String' is not a data class or has not enough arguments"
    }

    "data classes with too few arguments must not be instantiatable" {
        shouldThrow< IllegalArgumentException> {
            DataClassPattern4<Test3, String, Int, Int?, Boolean>()
        }.message shouldBe "'Test3' is not a data class or has not enough arguments"
    }
})