package kopama.example

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.capture.capture
import kopama.compare.eq
import kopama.compare.isA
import kopama.compare.lt
import kopama.match
import kopama.operators.and
import kopama.strings.startsWith

class ClassHierarchyTest : StringSpec({

    "works with classes in a hierarchy" {
        val p: LegalEntity = Person("John", "Doe", 27, Gender.MALE)

        match(p) {
            isA<String>() then { "no way!" }
            company(+"Acme") then { "impossible" }
            person(+"Jane", +"Doe", eq(27), +Gender.FEMALE) then { "oops" }
            person(+"John", startsWith("D"), eq(28), isA<Gender>()) then { "also oops" }
            val ageCapture = capture<Int>()
            person(
                +"John",
                +"Doe",
                ageCapture and lt(37),
                +Gender.MALE
            ) then { "It's John Doe, age ${ageCapture.value}" }
            otherwise { "Uninteresting person" }
        } shouldBe "It's John Doe, age 27"
    }
})
