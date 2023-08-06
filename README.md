# kopama

Kopama ("Kotlin Pattern Matching") provides pattern matching functionality, as known from Haskell and Scala. It not only supports built-in classes, but also custom data classes (although this requires the use of KSP).

WARNING: This library is a work in progress, it is not production ready.

## Example

Assume we need to contact users who have an ADMIN role. If they reside in the US, we want to prefer a contact by phone, otherwise we would rather prefer email. If no contact information is given, we inform the billing department.

```kotlin
data class User(
    val name:String,
    val country:String,
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
            "Inform billing, user: ${user.name}"
        else -> "No action"
    }
```

Using the kopama library, including the KSP features, we can rewrite the example as follows:

```kotlin
@Kopama
data class User(
    val name:String,
    val country:String,
    val roles: List<String>,
    val email: String? = null,
    val phone: String? = null
)

fun kopamaMethod(user: User): String =
    match(user) {
        user(country = +"US", 
            roles = contains("ADMIN"), 
            phone = !isNull) then
                { "Contact by phone: ${user.phone}" }
        user(roles = contains("ADMIN"), email = !isNull) then
                {  "Contact by email: ${user.email}" }
        user(roles = contains("ADMIN"), phone = !isNull) then
                { "Contact by phone: ${user.phone}" }
        user(roles = contains("ADMIN")) then
                { "Inform billing, user: ${user.name}" }
        otherwise { "no action" }
    }
```
If a data class is annotated with `@Kopama`, a pattern function like `user()` will be generated, which allows to check each component individually. There are many built-in check methods, but it is very easy to write your own. This approach is especially convenient when dealing with deeply nested structures. 

## Work in Progress

The example above should give only a rough idea. As the library is still under construction, I'll have to refer to the tests and example code. Once the dust settles, I'll include a more detailed description here, especially regarding KSP.

I'm thankful for any feedback.
