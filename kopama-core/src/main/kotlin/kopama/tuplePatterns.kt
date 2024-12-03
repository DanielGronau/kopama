package kopama

fun <A, B> pair(p: (A, B) -> Boolean): Pattern<Pair<A, B>> =
    { p(it.first, it.second) }

fun <A, B> pair(first: Pattern<A> = any, second: Pattern<B> = any): Pattern<Pair<A, B>> =
    { first(it.first) && second(it.second) }

// top-level version of pair, which avoids the need for explicit type parameters
fun <A, B> Matcher<Pair<A, B>, *>.pair_(first: Pattern<A> = any, second: Pattern<B> = any): Pattern<Pair<A, B>> =
    { first(it.first) && second(it.second) }

fun <A, B> first(p: Pattern<A>): Pattern<Pair<A, B>> =
    { p(it.first) }

fun <A, B> second(p: Pattern<B>): Pattern<Pair<A, B>> =
    { p(it.second) }

fun <A, B, C> triple(p: (A, B, C) -> Boolean): Pattern<Triple<A, B, C>> =
    { p(it.first, it.second, it.third) }

fun <A, B, C> triple(
    p1: Pattern<A> = any,
    p2: Pattern<B> = any,
    p3: Pattern<C> = any
): Pattern<Triple<A, B, C>> =
    { p1(it.first) && p2(it.second) && p3(it.third) }

// top-level version of triple, which avoids the need for explicit type parameters
fun <A, B, C> Matcher<Triple<A,B,C>,*>.triple_(
    p1: Pattern<A> = any,
    p2: Pattern<B> = any,
    p3: Pattern<C> = any
): Pattern<Triple<A, B, C>> =
    { p1(it.first) && p2(it.second) && p3(it.third) }

fun <A, B, C> triple1(p: Pattern<A>): Pattern<Triple<A, B, C>> =
    { p(it.first) }

fun <A, B, C> triple2(p: Pattern<B>): Pattern<Triple<A, B, C>> =
    { p(it.second) }

fun <A, B, C> triple3(p: Pattern<C>): Pattern<Triple<A, B, C>> =
    { p(it.third) }