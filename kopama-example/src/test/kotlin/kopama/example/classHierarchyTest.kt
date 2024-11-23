package kopama.example

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.Kopama
import kopama.and
import kopama.capture
import kopama.eq
import kopama.isA
import kopama.lt
import kopama.match
import kopama.startsWith

enum class Gender { MALE, FEMALE, DIVERSE }

sealed interface LegalEntity

@Kopama
data class Person(val firstName: String, val lastName: String, val age: Int, val gender: Gender) : LegalEntity

@Kopama
class Company(val companyName: String, var taxID: String, nonsense: String) : LegalEntity

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
