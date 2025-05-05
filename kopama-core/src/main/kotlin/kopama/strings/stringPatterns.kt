package kopama.strings

import kopama.Pattern

/**
 * Matches when the value has the given string representation (using `toString()`).
 *
 * @param string the required string representation.
 * @return the resulting pattern.
 */
fun hasToString(string: String): Pattern<Any> =
    { it.toString() == string }

/**
 * Matches if the string value is the same as the given string, ignoring upper- and lowercase.
 *
 * @param string the required string representation.
 * @return the resulting pattern.
 */
fun eqIgnoreCase(string: String): Pattern<String> =
    { it.equals(string, true) }

/**
 * Matches when the string value starts with the given string.
 *
 * @param string the required start of the string.
 * @return the resulting pattern.
 */
fun startsWith(string: String): Pattern<String> =
    { it.startsWith(string) }

/**
 * Matches when the string value ends with the given string.
 *
 * @param string the required end of the string.
 * @return the resulting pattern.
 */
fun endsWith(string: String): Pattern<String> =
    { it.endsWith(string) }

/**
 * Matches when the string value contains the given string.
 *
 * @param string the required part of the string.
 * @return the resulting pattern.
 */
fun containsString(string: String): Pattern<String> =
    { it.contains(string) }

/**
 * Matches when the string value has the given length.
 *
 * @param length the required string length.
 * @return the resulting pattern.
 */
fun hasLength(length: Int): Pattern<String> =
    { it.length == length }

/**
 * Matches when the length of the string value matches the given pattern.
 *
 * @param pattern the pattern which must hold for the string length.
 * @return the resulting pattern.
 */
fun hasLength(pattern: Pattern<Int>): Pattern<String> =
    { pattern(it.length)}

/**
 * Matches when the string value matches the given regex.
 *
 * @param regex the regex to test the string against.
 * @return the resulting pattern.
 */
fun regex(regex: String): Pattern<String> =
    { it.matches(regex.toRegex()) }
