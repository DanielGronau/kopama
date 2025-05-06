package kopama.dataclasses

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.compare.eq
import kopama.compare.isNull
import kopama.match

class DataClassPatternTest: StringSpec({

    data class Test3(val foo: String, var bar: Int, val baz: Int?)

    val test3 = DataClassPattern3<Test3, String, Int, Int?>(Test3::class)

    "data class patterns should match correctly" {
        match(Test3("quux", 42, null)) {
            test3(+"quux", eq(12), isNull) then { "wrong" }
            test3(+"quux", comp3 = eq(12)) then { "wronger" }
            test3(comp3 = eq(1)) then { "wrongest" }
            test3(+"quux", eq(42), isNull) then { "yupp" }
            otherwise { "unknown" }
        } shouldBe "yupp"
    }
})