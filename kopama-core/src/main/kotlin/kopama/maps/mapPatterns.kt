package kopama.maps

import kopama.Pattern

/**
 * Matches when the keys of the map match the given pattern.
 *
 * @param K the key type of the map.
 * @param V the value type of the map.
 * @param pattern the pattern to match the key set against.
 * @return the resulting pattern.
 */
fun <K, V> keys(pattern: Pattern<Set<K>>): Pattern<Map<K, V>> =
    { pattern(it.keys) }

/**
 * Matches when the values of the map match the given pattern.
 *
 * @param K the key type of the map.
 * @param V the value type of the map.
 * @param pattern the pattern to match the values against.
 * @return the resulting pattern.
 */
fun <K, V> values(pattern: Pattern<Collection<V>>): Pattern<Map<K, V>> =
    { pattern(it.values) }

/**
 * Matches when the entries of the map match the given pattern.
 *
 * @param K the key type of the map.
 * @param V the value type of the map.
 * @param pattern the pattern to match the entries against.
 * @return the resulting pattern.
 */
fun <K, V> entries(pattern: Pattern<List<Pair<K, V>>>): Pattern<Map<K, V>> =
    { pattern(it.entries.map(Map.Entry<K, V>::toPair)) }

/**
 * Matches when the value for a certain key matches the given pattern.
 *
 * Example usage:
 *
 * ```
 * match(mapOf(1 to "foo", 2 to "bar", 3 to "quux")) {
 *    ...
 *    startsWith("b").valueAt(2) then "value for key '2' starts with 'b'"
 *    ...
 * }
 * ```
 *
 * @param K the key type of the map.
 * @param V the value type of the map.
 * @param key the key specifying which value should be matched against the pattern.
 * @return the resulting pattern.
 */
fun <K,V> Pattern<V>.valueAt(key: K): Pattern<Map<K, V>> =
    { it[key]?.let { this@valueAt(it) } == true }

/**
 * Matches if the map has the given size.
 *
 * @param size the required map size.
 * @return the resulting pattern.
 */
fun mapHasSize(size: Int): Pattern<Map<*, *>> =
    { it.size == size }

/**
 * Matches if the map size matches the given pattern.
 *
 * @param pattern the pattern defining a valid map size.
 * @return the resulting pattern.
 */
fun mapHasSize(pattern: Pattern<Int>): Pattern<Map<*, *>> =
    { pattern(it.size) }