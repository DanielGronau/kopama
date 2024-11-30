package kopama

val any: Pattern<Any?> =
    { true }

val none: Pattern<Any?> =
    { false }

// These versions can be used to explicitly specify a type in order to avoid ambiguity,
// e.g. when matching a generic class

fun <P> any(): Pattern<P> = { true }

fun <P> none(): Pattern<P> = { false }
