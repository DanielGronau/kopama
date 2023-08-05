package kopama

typealias Pattern<P> = (P) -> Boolean

interface MatchResult<T : Any> {
    val value: T
}

class Matcher<P, T : Any>(val obj: P) {

    var result: T? = null

    inline infix fun <reified Q : P> Pattern<Q>.then(value: () -> T) {
        if (result == null && obj is Q && this(obj)) {
            result = value()
        }
    }

    operator fun Any.unaryPlus() = eq(this)

    fun otherwise(default: () -> T) = object : MatchResult<T> {
        override val value = result ?: default()
    }
}

fun <P, T : Any> match(obj: P, body: Matcher<P, T>.() -> MatchResult<T>): T =
    Matcher<P, T>(obj).run(body).value