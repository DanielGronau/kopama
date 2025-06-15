package kopama.example

import kopama.Kopama
import kopama.ValidationResult
import kopama.collections.contains
import kopama.collections.containsAny
import kopama.compare.any
import kopama.compare.isNull
import kopama.compare.isNullOr
import kopama.compare.oneOf
import kopama.dataclasses.DataClassPattern5
import kopama.match
import kopama.operators.not
import kopama.strings.containsString
import kopama.validate

@Kopama
data class User(
    val name: String,
    val country: String,
    val roles: List<String>,
    val email: String? = null,
    val phone: String? = null
)

fun manualMethod(user: User): String =
    when {
        user.country == "US" && user.roles.contains("ADMIN") && user.phone != null ->
            "Contact by phone: ${user.phone}"

        user.roles.contains("ADMIN") && user.email != null ->
            "Contact by email: ${user.email}"

        user.roles.contains("ADMIN") && user.phone != null ->
            "Contact by phone: ${user.phone}"

        user.roles.contains("ADMIN") ->
            "Inform billing, user: ${user.name}"

        else -> "no action"
    }

fun kopamaMethod(user: User): String =
    match(user) {
        user(any, +"US", contains("ADMIN"), any, !isNull) then
                { "Contact by phone: ${user.phone}" }
        user(roles = contains("ADMIN"), email = !isNull) then
                { "Contact by email: ${user.email}" }
        user(roles = contains("ADMIN"), phone = !isNull) then
                { "Contact by phone: ${user.phone}" }
        user(roles = contains("ADMIN")) then
                { "Inform billing, user: ${user.name}" }
        otherwise { "no action" }
    }

fun dataMethod(user: User): String {
    val user_ = DataClassPattern5<User, String, String, List<String>, String?, String?>(User::class)

    return match(user) {
        user_(any, +"US", contains("ADMIN"), any, !isNull) then
                { "Contact by phone: ${user.phone}" }
        user_(comp3 = contains("ADMIN"), comp4 = !isNull) then
                { "Contact by email: ${user.email}" }
        user_(any, any, contains("ADMIN"), any, !isNull) then
                { "Contact by phone: ${user.phone}" }
        user_(comp3 = contains("ADMIN")) then
                { "Inform billing, user: ${user.name}" }
        otherwise { "no action" }
    }
}

fun userValidation(user: User): ValidationResult<String> {
    val result = validate(user) {
        user(name = containsString(" ")) onFail
                { "must have first and last name" }
        user(country = oneOf("UK", "US")) onFail
                { "must be from UK or US" }
        user(roles = containsAny("ADMIN", "SUPERUSER")) onFail
                { "must be admin or superuser" }
        user(phone = isNullOr(!containsString("-"))) onFail
                { "phone must not contain -" }
    }
    return result
}

fun main() {
    val user = User("John", "DE", listOf("ADMIN"), phone = "123-456-789")

    val c1 = manualMethod(user)
    println(c1)

    val c2 = kopamaMethod(user)
    println(c2)

    val c3 = dataMethod(user)
    println(c3)

    val result = userValidation(user)
    println("User is valid: ${result.isValid()}")
    println("Problems: ${result.failures}")
}




