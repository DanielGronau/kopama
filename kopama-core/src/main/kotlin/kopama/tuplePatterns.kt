package kopama

fun <A, B> pair(p: (A, B) -> Boolean): Pattern<Pair<A, B>> =
    { p(it.first, it.second) }

fun <A, B> pair(pa: Pattern<A>, pb: Pattern<B>): Pattern<Pair<A, B>> =
    { pa(it.first) && pb(it.second) }

fun <A, B> first(p: Pattern<A>): Pattern<Pair<A, B>> =
    { p(it.first) }

fun <A, B> second(p: Pattern<B>): Pattern<Pair<A, B>> =
    { p(it.second) }

fun <A, B, C> triple(p: (A, B, C) -> Boolean): Pattern<Triple<A, B, C>> =
    { p(it.first, it.second, it.third) }

fun <A, B, C> triple(pa: Pattern<A>, pb: Pattern<B>, pc: Pattern<C>): Pattern<Triple<A, B, C>> =
    { pa(it.first) && pb(it.second) && pc(it.third) }

fun <A, B, C> triple1(p: Pattern<A>): Pattern<Triple<A, B, C>> =
    { p(it.first) }

fun <A, B, C> triple2(p: Pattern<B>): Pattern<Triple<A, B, C>> =
    { p(it.second) }

fun <A, B, C> triple3(p: Pattern<C>): Pattern<Triple<A, B, C>> =
    { p(it.third) }