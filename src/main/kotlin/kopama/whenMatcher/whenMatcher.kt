package kopama.whenMatcher

import kopama.Matching
import kopama.Pattern

fun match(obj: Any?): Matching = object : Matching {
    override fun equals(other: Any?) =
        when (other) {
            is Pattern -> other.test(obj)
            else -> false
        }
}

