package kopama

internal class StringPattern(private val string: String, private val fn: (String, String) -> Boolean) : Pattern {
    override fun test(obj: Any?) = when (obj) {
        is String -> fn.invoke(obj, string)
        else -> false
    }
}

fun eqIgnoreCase(string: String): Pattern = StringPattern(string) { obj, s -> obj.equals(s, true) }

fun contains(string: String): Pattern = StringPattern(string) { obj, s -> obj.contains(s) }

fun startsWith(string: String): Pattern = StringPattern(string) { obj, s -> obj.startsWith(s) }

fun endsWith(string: String): Pattern = StringPattern(string) { obj, s -> obj.endsWith(s) }

fun regex(string: String): Pattern = StringPattern(string) { obj, s -> obj.matches(s.toRegex()) }
