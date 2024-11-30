package kopama

fun <P> hasToString(string: String): Pattern<P> =
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

fun regex(regex: String): Pattern<String> =
    { it.matches(regex.toRegex()) }
