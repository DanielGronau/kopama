package kopama.core

import java.io.Serializable

typealias Pattern<P> = (P) -> Boolean

interface MatchResult<T : Any> {
    val value: T
}

class Matcher<P, T : Any>(private val obj: P) {

    private var result: T? = null

    infix fun Pattern<P>.then(value: () -> T) {
        if (result == null && this(obj)) {
            result = value()
        }
    }

    fun otherwise(default: () -> T) = object : MatchResult<T> {
        override val value = result ?: default()
    }
}

fun <P, T : Any> match(obj: P, body: Matcher<P, T>.() -> MatchResult<T>): T =
    Matcher<P, T>(obj).run(body).value

data class Person(val firstName: String, val lastName: String, val age: Int)

fun person(
    firstNamePattern: Pattern<String>,
    lastNamePattern: Pattern<String>,
    agePattern: Pattern<Int>
): Pattern<Person?> = {
    when (it) {
        null -> false
        else -> firstNamePattern(it.firstName) &&
                lastNamePattern(it.lastName) &&
                agePattern(it.age)
    }
}

fun main() {
    //val p: Person? = null
    val p: Person = Person("Andy", "Smith", 42)

    val result = match(p) {
        eq("Andy Smith") on {p: Person -> "${p.firstName} ${p.lastName}"} then { "It's Adam Smith"}

        person(oneOf("Andy", "Mike"), eq("Miller"), any()) then
                { "One of the Miller brothers" }

        person(any(), isInstance(Serializable::class), gt(50)) then
                { "An old person!" }

        val ageCap = capture<Int>()
        person(eq("Andy"), !eq("Miller"), ageCap) then
                { "Some unknown Andy of age ${ageCap.value}" }

        isNull<Person>() then { "Null-Value" }

        otherwise { "Some unknown person" }
    }

    println(result)

    val r = match(listOf(1,2,4, 42)) {
        eq(2)[1] then { "Second element is 2"}
        all(lt(10) or eq(42)) then { "Only small elements" }
        otherwise { "no match" }
    }

    println(r)
}