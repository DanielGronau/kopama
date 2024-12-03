package kopama

fun hasToString(string: String): Pattern<Any> =
    { it.toString() == string }

fun eqIgnoreCase(string: String): Pattern<String> =
    { it.equals(string, true) }

fun startsWith(string: String): Pattern<String> =
    { it.startsWith(string) }

fun endsWith(string: String): Pattern<String> =
    { it.endsWith(string) }

fun containsString(string: String): Pattern<String> =
    { it.contains(string) }

fun hasLength(length: Int): Pattern<String> =
    { it.length == length }

fun hasLength(pattern: Pattern<Int>): Pattern<String> =
    { pattern(it.length)}

fun regex(regex: String): Pattern<String> =
    { it.matches(regex.toRegex()) }
