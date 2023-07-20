package kopama.example

import kopama.core.*


enum class Gender { MALE, FEMALE, DIVERSE }

sealed interface LegalEntity

@Kopama
data class Person(val firstName: String, val lastName: String, val age: Int, val gender: Gender): LegalEntity

@Kopama
data class Company(val companyName: String, val taxID: String)

fun main() {
    val p:LegalEntity = Person("John", "Doe", 27, Gender.MALE)

    val r1 = match(p) {
        -company(+"Acme") then { TODO() }
        -person(+"Jane", +"Doe", eq(27), +Gender.FEMALE) then { "oops" }
        -person(+"John", +"Doe", eq(28)) then { "also oops" }
        -person(+"John", startsWith("D"), lt(30), +Gender.MALE) then { "It's John Doe" }
        otherwise { "Uninteresting person" }
    }

    println(r1)
}

