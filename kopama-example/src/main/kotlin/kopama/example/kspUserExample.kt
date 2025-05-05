package kopama.example

import kopama.*
import kopama.collections.contains
import kopama.compare.isNull
import kopama.compare.any
import kopama.operators.not

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

fun main() {
    val user = User("Daniel", "DE", listOf("ADMIN"), phone = "123-456-789")

    val c1 = manualMethod(user)
    println(c1)

    val c2 = kopamaMethod(user)
    println(c2)
}