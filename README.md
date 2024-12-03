# kopama

Kopama ("Kotlin Pattern Matching") provides pattern matching functionality, as known from Haskell and Scala. It not only
supports built-in classes, but also custom classes (which requires the use of KSP). The project started out as an
example in my
book [Creative DSLs in Kotlin](https://www.amazon.com/-/de/dp/3759759866/) ([eBook](https://play.google.com/store/books/details/Daniel_Gronau_Creative_DSLs_in_Kotlin?id=ZtMZEQAAQBAJ)).

## Introduction

Assume we need to contact users who have an ADMIN role. If they reside in the US, we want to prefer a contact by phone,
otherwise we would rather prefer email. If no contact information is given, we inform the billing department.

```kotlin
data class User(
    val name: String,
    val country: String,
    val roles: List<String>,
    val email: String? = null,
    val phone: String? = null
)

fun contactMethod(user: User): String =
    when {
        user.country == "US"
                && user.roles.contains("ADMIN")
                && user.phone != null ->
            "Contact by phone: ${user.phone}"
        user.roles.contains("ADMIN") && user.email != null ->
            "Contact by email: ${user.email}"
        user.roles.contains("ADMIN") && user.phone != null ->
            "Contact by phone: ${user.phone}"
        user.roles.contains("ADMIN") ->
            "Inform billing department, user: ${user.name}"
        else -> "No action"
    }
```

Using the kopama library, including the KSP features, we can rewrite the example as follows:

```kotlin
@Kopama
data class User(
    val name: String,
    val country: String,
    val roles: List<String>,
    val email: String? = null,
    val phone: String? = null
)

fun kopamaMethod(user: User): String =
    match(user) {
        user(any, +"US", contains("ADMIN"), any, !isNull) then
                { "Contact by phone: ${user.phone}" }
        user(roles = contains("ADMIN"), email = !isNull) then
                { "Contact by email: ${user.email}" }
        user(roles = contains("ADMIN"), phone = !isNull) then
                { "Contact by phone: ${user.phone}" }
        user(roles = contains("ADMIN")) then
                { "Inform billing department, user: ${user.name}" }
        otherwise { "no action" }
    }
```

If a final class is annotated with `@Kopama`, a pattern function like `user()` will be generated (via KSP), which allows
to check each component individually, either by using one of the built-in patterns (like `contains()`), or by using a
nested generated pattern (e.g. imagine that the email in the example was not a simple String but has its own data class), or
by a custom pattern function. It is also possible to capture values in the process, which can then be accessed on the
right-hand side.

## Using the Library

In order to use the core functionality with the built-in patterns, add the dependency:

`implementation("kopama:kopama-core:$kopamaVersion")`

If you also want to generated patterns, add the depencency:

`ksp("kopama:kopama-ksp:$kopamaVersion")`

NOTE: Currently Kopama is not available on Maven Central, you can use [JitPack](https://jitpack.io/) instead. Once the
library is stable and production-ready, it will be released on Maven Central.

## Considerations before using Kopama

Despite all efforts to make the library as safe to use as possible, there are still some risks involved:

* the code generation is complicated and might break if a class is not suitable, or was updated without checking that
  the matching still works. Renaming, reordering or removing a property can break the code, as well as using extension
  properties or functions.
* generating code from generic classes is especially tricky, and it is hard to predict if classes with complicated type
  signatures (e.g. recursive ones) can be processed properly
* capturing variables is inherently unsafe, e.g. the captured value is readable even after leaving the `then` branch, or
  it will be overridden if the capture variable is used multiple times in a pattern

Please, always check the generated code, and take extra care when changing classes used for code generation, and when
capturing variables.

## Match Expressions

The general structure of a match-expression is

```kotlin
  match(<value>) {
      <pattern> then { <result> }
      <pattern> then { <result> }
      <pattern> then { <result> }
      ...
      otherwise { <result> }
  }
```

A pattern is just a predicate `(T) -> Boolean` - the library uses the typealias `Pattern<T>`. The result value of the
whole expression will be determined by the first `then`-branch with a matching pattern, or the result of the `otherwise`
-branch when none was matching. The `otherwise`-branch is mandatory, as there is no way for the library to determine if
the given patterns are exhaustive.

The naming of the built-in patterns often follows corresponding Hamcrest matchers, so if you are familiar with the
latter (e.g. by using them for testing), you should pick up the Kopama patterns quickly.

## Built-In Patterns

### Constant Patterns

There are two constant patterns:

* `any`, which is always `true`
* `none`, which is always `false`.

For generated pattern functions like `user()` in the introduction example, all the patterns for the component patterns
are by default `any`.

### Operator Patterns

Operator patterns allow to combine or modify existing patterns:

* infix functions like `and` and `or`
* the functions `allOf`, `anyOf` and `noneOf` for combining several patterns
* the operator `on` which allows to transform a value before the given pattern is applied
* the conditional operators `thenRequire` and `ifThenElse`, which test patterns only if an initial condition is met

### Comparing Patterns

Comparing patterns perform some kind of comparison:

* `isNull` and `isNotNull` check against `null`, `isNullOr` and `isNotNullAnd` combine a not nullable pattern with a
  null-check
* `eq` checks if the value is equal with a given one. Inside a `match` block, the unary `+` is an alternative way to
  call the pattern, e.g. `+"John"` is a shorthand for `eq("John")`. Obviously, for numerical values you still need to
  use `eq`, as they have already a more specific implementation of the unary `+`. If you need instance equality, you can
  use `isSame`
* `oneOf` checks if the value is equal to one of the given ones
* `isA` checks if the value has a given type
* `gt`, `ge`, `lt` and `le` check if the value is greater than, greater or equal, less then, or less or equal to the
  given value. This works only for comparable values.
* `between` checks if a value is between the given values, and `inRange` checks if the value falls in a given closed
  range

### Collection and Array Patterns

Collection patterns work on collections or sometimes on iterables:

* `isEmpty` and `isNotEmpty` check whether the collection is empty or not
* `hasSize` checks if the collection has the given size
* `forAll`, `forAny` and `forNone` check the given pattern for all elements of the iterable
* `contains`, `containsAll`, `containsAny`, `containsAll` check if the elements match the given values
* the indexed access operator `[]` matches the pattern with the list element specified by the index

Array patters are very similar, they have usually just the prefix `array...`. One exception is the indexed access, which
is replaced by the `atIndex` function.

### Map Patterns

* `keys`, `values` and `entries` apply the pattern on the specified part of the map
* `valueAt` applies a pattern to the value associated with the key
* `mapHasSize` checks if the map has the given size

### String Patterns

* `hasToString` checks if calling `toString` on a value gives the expected result
* `eqIgnoreCase` compares two strings ignoring lower- and uppercase spelling
* `startsWith`, `endsWith` and `containsString` checks if the string contains the given partial string
* `hasLength` checks if a string has the given length
* `regex` checks if the string matches a given regex

### Tuple Patterns

Tuple patterns work on pairs and triples:

* `pair` and `triple` check all elements against the given pattern. It might be required to specify type parameters for these patterns. If this happens when using them as top-level patterns (not inside other patterns), using the versions `pair_` and `triple_` instead should allow to avoid type parameters in most cases
* `first`, `second`, `triple1`, `triple2` and `triple3` match the specified element

## Capturing Values

If you want to use the value of a nested property of the matched value on the right-hand side of `then`, you can use variable capture. First, you need to define a capture pattern before the test branch you want to use it in, e.g. `val capInt = capture<Int>()`. In the nested pattern on the left-hand side, you can then capture the value by using the defined pattern. On the right-hand side you can then read the value from the capture pattern, e.g. `capInt.value`. Note that you can't read null values, and that an error is thrown if you try to read a capture pattern before a value was captured.

You can have multiple capture patterns, and they can be reused. Of course, you have to be careful that you actually use the pattern on the left-hand side, else you will read values captured in earlier branches. Here is an example:

```kotlin
val s = match(12 to "twelve") {
    val capInt = capture<Int>()
    val capStr = capture<String>()
    pair(capInt, capStr and hasLength(17)) then {
        "first: ${capInt.value} second: ${capStr.value} and length is 17"
    }
    pair(capInt, capStr and hasLength(6)) then { 
        "first: ${capInt.value} second: ${capStr.value} and length is 6"
    }
    otherwise { "oops" }
} 
println(s) //  "first: 12 second: twelve and length is 6"
```
Please avoid the following mistakes:
* Don't try to read the value of a capture pattern before it was used
* Make sure that the capture pattern was actually captured on the left-hand side before reading the value on the right-hand side
* Just because a capture pattern appears on the left-hand side doesn't mean it will always be captured, as some operations short-circuit. E.g. code like `any or capInt` would never capture a value
* Don't capture the same pattern twice in one left-hand side, it will then contain only the last value

Generally avoid capturing values which can be easily obtained from the matched value itself. Use the feature instead to read deeply nested or otherwise hard to get values.

## Generated Patterns

### Generated Patterns for Classes with Type Variables