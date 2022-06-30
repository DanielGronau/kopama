# kopama

Kopama ("Kotlin pattern matching") enables some pattern matching
, e.g. in `when` expressions. 
The syntax is similar to Hamcrest matchers, but shortened.

### Example

```kotlin
data class Person(val firstName: String, val lastName: String, val age: Int)

val p = Person("Alice", "Cooper", 74)

when(match(p)) {
    "Alien"(any, any, any) -> println("Aliens!")
    "Person"(eq("Mick"), eq("Jagger"), eq(78)) -> println("Mick Jagger!")
    "Person"(eq("Alice"), eq("Cooper"), any) -> println("Alice Cooper!")
    else -> println("I don't know this guy")
}
```

### Implementation

The deconstruction for data classes is based on their `componentN` methods,
and for `Iterable` classes on their elements. The
patterns are not checked against type or range of the given object,
the pattern will simply not match in this case, but there will be
(hopefully) neither compile-time nor runtime-errors.

### Warning

I don't consider this library ready for use in production. 
I did my best to test for the intended behavior,
but with such ultra-flexible and rule-bending code it's hard to be
absolutely sure that all edge cases behave in a sensible way. Further,
the code uses reflection and might be too slow for your use case.

Also, I should mention that "kopama" means "anger" in Telugu.

### Last words

I hope you have as much fun playing with the library as I have 
writing it.