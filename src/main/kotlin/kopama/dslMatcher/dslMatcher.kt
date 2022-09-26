package kopama.dslMatcher

import kopama.Pattern

data class MatchResult<T>(val value: T)

class Matcher<T>(private val obj: Any?) {
    val captures = mutableMapOf<String, Any?>()
    private var result: T? = null

    fun otherwise(default: () -> T) = MatchResult(result ?: default())

    infix fun Pattern.then(value: () -> T) {
        captures.clear()
        if (result == null && this.test(obj)) {
            result = value()
        }
    }
}

fun <T> match(obj: Any, body: Matcher<T>.() -> MatchResult<T>): T =
    Matcher<T>(obj).run(body).value
