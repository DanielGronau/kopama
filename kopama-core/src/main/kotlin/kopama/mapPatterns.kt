package kopama

fun <K, V> keys(p: Pattern<Set<K>>): Pattern<Map<K, V>> =
    { p(it.keys) }

fun <K, V> values(p: Pattern<Collection<V>>): Pattern<Map<K, V>> =
    { p(it.values) }

fun <K, V> entries(p: Pattern<List<Pair<K, V>>>): Pattern<Map<K, V>> =
    { p(it.entries.map(Map.Entry<K, V>::toPair)) }

fun <K,V> Pattern<V>.valueAt(key: K): Pattern<Map<K,V>> =
    { it[key]?.let { this@valueAt(it) } == true }

fun mapHasSize(size: Int): Pattern<Map<*, *>> =
    { it.size == size }

fun mapHasSize(pattern: Pattern<Int>): Pattern<Map<*, *>> =
    { pattern(it.size) }