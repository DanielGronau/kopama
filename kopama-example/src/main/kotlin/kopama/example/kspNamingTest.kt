package kopama.example

import kopama.Kopama
import kopama.any
import kopama.eq
import kopama.match
import java.time.Instant

@Kopama(patternName = "bar", patternFileName = "bazPattern")
data class Foo(val alpha: Int, val beta: String, val gamma: Instant)

fun main() {
    val foo = Foo(12, "twelve", Instant.now())
    val s = match(foo) {
        bar(eq(12), +"eleven", any) then { "nope" }
        bar(eq(12), +"twelve", any) then { "yupp" }
        otherwise { "oops" }
    }
    println(s)
}

