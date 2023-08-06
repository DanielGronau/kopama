package kopama.example

import kopama.*


enum class Gender { MALE, FEMALE, DIVERSE }

sealed interface LegalEntity

@Kopama
data class Person(val firstName: String, val lastName: String, val age: Int, val gender: Gender) : LegalEntity

@Kopama
data class Company(val companyName: String, val taxID: String) : LegalEntity

fun main() {
    val p: LegalEntity = Person("John", "Doe", 27, Gender.MALE)

    val result = match(p) {
        isA<String>() then { "no way!" }
        company(+"Acme") then { "impossible" }
        person(+"Jane", +"Doe", eq(27), +Gender.FEMALE) then { "oops" }
        person(+"John", startsWith("D"), eq(28), isA<Gender>()) then { "also oops" }
        person(+"John", +"Doe", lt(30), +Gender.MALE) then { "It's John Doe" }
        otherwise { "Uninteresting person" }
    }

    println(result)
}

