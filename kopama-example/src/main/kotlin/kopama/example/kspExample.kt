package kopama.example

import kopama.core.*


enum class Gender { MALE, FEMALE, DIVERSE }

@Kopama
data class Person(val firstName: String, val lastName: String, val age: Int, val gender: Gender)

fun main() {
    val p = Person("John", "Doe", 27, Gender.MALE)

    val r1 = match(p) {
        person(eq("John"), startsWith("D"), lt(30), eq(Gender.MALE)) then { "It's John" }
        otherwise { "Uninteresting person" }
    }

    println(r1)
}
