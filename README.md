# Kopama

Kopama ("**Ko**tlin **Pa**ttern **Ma**tching") provides pattern matching functionality, as known from Haskell and Scala. It not only supports built-in classes, but also custom classes (which requires the use of KSP). The project started out as an example for my book [Creative DSLs in Kotlin](https://www.amazon.com/-/de/dp/3759759866/) ([eBook](https://play.google.com/store/books/details/Daniel_Gronau_Creative_DSLs_in_Kotlin?id=ZtMZEQAAQBAJ)).

## Introduction

Assume we need to contact users who have an ADMIN role. If they reside in the US, we want to prefer a contact by phone, otherwise we would rather prefer email. If no contact information is given, we inform the billing department.

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

If a final class is annotated with `@Kopama`, a pattern function like `user()` will be generated (via KSP), which allows to check each component individually, either by using one of the built-in patterns (like `contains()`), or by using a nested generated pattern (e.g. imagine that the email in the example was not a simple String but has its own data class), or by a custom pattern function. It is also possible to capture values in the process, which can then be accessed on the right-hand side.

## Using the Library

In order to use the core functionality with the built-in patterns, add the dependency:

`implementation("kopama:kopama-core:$kopamaVersion")`

If you also want to generated patterns, add the dependency:

`ksp("kopama:kopama-ksp:$kopamaVersion")`

NOTE: Currently Kopama is not available on Maven Central, you can use [JitPack](https://jitpack.io/) instead. Once the
library is stable and production-ready, it will be released on Maven Central.

## Considerations before using Kopama

Despite all efforts to make the library as safe to use as possible, there are still some risks involved:

* the code generation is complicated and might break if a class is not suitable, or was updated without checking that the matching still works. Renaming, reordering or removing a property can break the code, as well as using extension  properties or functions.
* generating code from generic classes is especially tricky, and it is hard to predict if classes with complicated type signatures (e.g. recursive ones) can be processed properly
* capturing variables is inherently unsafe, e.g. the captured value is readable even after leaving the `then` branch, or it will be overridden if the capture variable is used multiple times in a pattern

Please, always check the generated code, and take extra care when changing classes used for code generation, and when capturing variables.

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

A pattern is just a predicate `(T) -> Boolean` - the library uses the typealias `Pattern<T>`. The result value of the whole expression will be determined by the first `then`-branch with a matching pattern, or the result of the `otherwise`-branch when none was matching. The `otherwise`-branch is mandatory, as there is no way for the library to determine if the given patterns are exhaustive.

The naming of the built-in patterns often follows the corresponding [Hamcrest Matchers](https://hamcrest.org/JavaHamcrest/) (e.g. used in testing libraries), so if you are familiar with them, you should pick up the Kopama patterns quickly.

## Built-In Patterns

### Comparing Patterns

Comparing patterns perform some kind of comparison:

* `any`, which is always `true`
* `none`, which is always `false`
* `isNull` and `isNotNull` check against `null`, `isNullOr` and `isNotNullAnd` combine a not nullable pattern with a null-check
* `eq` checks if the value is equal with a given one. Inside a `match` block, the unary `+` is an alternative way to call the pattern, e.g. `+"John"` is a shorthand for `eq("John")`. Obviously, for numerical values you still need to use `eq`, as they have already a more specific implementation of the unary `+`. If you need instance equality, you can use `isSame`
* `oneOf` checks if the value is equal to one of the given ones
* `isA` checks if the value has a given type
* `gt`, `ge`, `lt` and `le` check if the value is greater than, greater or equal, less then, or less or equal to the given value. This works only for comparable values.
* `between` checks if a value is between the given values, and `inRange` checks if the value falls in a given closed range

### Operator Patterns

Operator patterns allow to combine or modify existing patterns:

* infix functions like `and` and `or`
* the functions `allOf`, `anyOf` and `noneOf` for combining several patterns
* the operator `on` which allows to transform a value before the given pattern is applied
* the conditional operators `thenRequire` and `ifThenElse`, which test patterns only if an initial condition is met

### Collection and Array Patterns

Collection patterns work on collections or sometimes on iterables:

* `isEmpty` and `isNotEmpty` check whether the collection is empty or not
* `hasSize` checks if the collection has the given size
* `forAll`, `forAny` and `forNone` check the given pattern for all elements of the iterable
* `contains`, `containsAll`, `containsAny`, `containsAll` check if the elements match the given values
* the indexed access operator `[]` matches the pattern with the list element specified by the index

Array patters are very similar, the patterns have just an `array...` prefix. One exception is the indexed access, which is replaced by the `atIndex` function.

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

## Data Class Patterns

The recommended way to generate patterns for custom classes is to use the `@Kopama` annotation and the KSP module. However, the library provides a second way especially for data classes. It is less flexible, less safe and less convenient, but it doesn't rely on code generation. At the moment, only data classes up to eight parameters are supported.

To get a pattern template, you instantiate one of the `DataClassPattern1..8` classes with the parameter types of the data class. Mistakes during this step can lead to runtime errors. Then you can generate patterns by simply invoking the template. In contrast to the KSP version, the sub-patterns have fixed names `comp1..8`, which makes it less convenient to use named arguments. Here is an example:

```kotlin
data class Person(val firstName: String, val lastName: String, val age: Int)

// create a data class pattern template
val person = DataClassPattern3<Person, String, String, Int>(Person::class)

val result = match(Person("John", "Doe", 34)) {
    person(comp3 = lt(18)) then { "too young" }
    person(+"Jane", +"Doe", eq(27)) then { "it's Jane"}
    person(+"John", +"Doe", lt(50)) then { "it's John"}
    otherwise { "unknown person" }
}
println(result) // "it's John"
```
 
## Capturing Values

If you want to use the value of a nested property of the matched value on the right-hand side of `then`, you can use variable capture. First, you need to define a capture pattern before the test branch you want to use it in, e.g. `val capInt = capture<Int>()`. In the nested pattern on the left-hand side, you can then capture the value by using the defined pattern. On the right-hand side you can then read the value from the capture pattern, e.g. `capInt.value`. Note that an error is thrown if you try to read a capture pattern before a value was captured.

Sometimes it is not clear whether a variable is captured: Consider a pattern like `isNotNullAnd(capInt)`, which will short-circuit if the variable is `null`, so there is nothing captured in this case. To safely access the value in such situations, you have two options:
* check if `capInt.isSet` is `true` before accessing `capInt.value`
* use the `capInt.getOrNull()` function, which will return `null` for unset values

If you use a capture together with other patterns, make sure that the capture is evaluated first, e.g. write `capInt and ge(13)`, not the other way around.

You can have as many capture patterns as you want, and they can be reused in several branches. Of course, you have to be careful that you actually use the pattern on the left-hand side, else you will read values captured in earlier branches. Here is an example:

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
* Just because a capture pattern appears on the left-hand side doesn't mean it will always be captured, as some operations short-circuit. E.g. code like `any or capInt` would never capture a value. That's why you should evaluate a capture as early as possible
* Don't capture the same pattern twice in one left-hand side, it will then contain only the last value

Generally avoid capturing values which can be easily obtained from the matched value itself. Use the feature instead to read deeply nested or otherwise hard to get values.

## Generated Patterns

As a prerequisite for generating patterns for classes, you need to add a dependency to the KSP module of the library, e.g. in Gradle:

`ksp("kopama:kopama-ksp:$kopamaVersion")`

At least as long as you change your module frequently, it might be helpful to turn off  the KSP incremental build mode. For this, add the line `ksp.incremental=false` in your `gradle.properties` file.

After these preparations, you should be able to use the `@Kopama` annotation in order to generate a pattern for a class. If you use it for data and value classes, this will almost always "just work".

The `@Kopama` annotation has three arguments:
* `arguments: Array<String>`: Here you can specify the properties (both `val` and `var`) or no-argument functions that should appear in the pattern (in the given order). If you don't specify `arguments`, the generator will take all `val` and `var` parameters from the primary constructor of the class instead.
* `patternName: String`: This argument allows you to give the pattern a name. Be careful to avoid name clashes. If no `patternName` is given, the "de-capitalized" class name is used, e.g. the pattern for class `ExampleClass` would be named `exampleClass`.
* `fileName: String`: This argument can change the file name for the generated pattern function. If `fileName` is not given, it will use the pattern name and add `"Pattern"` as suffix, so the `exampleClass` pattern would be generated in a file named `exampleClassPattern`. Note that the file is generated in the package of the original class, and currently there is no way to change this. Also, it is not possible to have one file for multiple pattern functions.

### Generated Patterns for Generic Classes

The code generator works for _simple_ generic classes, but frankly, this is the part of the library I'm the least confident of. 

An important limitation is that the generator doesn't detect whether a type parameter is used for any of the pattern arguments, the pattern function will always have the same generic signature as the annotated class. Finding out which type parameter is really "used" is a challenging problem, as type parameters can also depend on each other. If this behavior is a problem, I would recommend to copy the generated pattern over to the regular code and to manually remove the offending type parameters from the function, and the `@Kopama` annotation from the class itself.

## Closing Remarks 

Working on this library was and is a fun challenge. Thank you for trying it out. I'm  grateful for your feedback. My ultimate goal would be to make this library superfluous, by helping to push the Kotlin language itself towards better pattern matching capabilities. 

Also, I would like to mention that "Kopama" means "Are you angry?" in Telugu.