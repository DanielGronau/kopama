package kopama.example

import kopama.*

@Kopama
data class User(
    val name:String,
    val country:String,
    val roles: List<String>,
    val email: String? = null,
    val phone: String? = null
)

fun contactMethod(user: User): String =
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
        user(country = +"US", roles = contains("ADMIN"), phone = !isNull) then
                { "Contact by phone: ${user.phone}" }
        user(roles = contains("ADMIN"), email = !isNull) then
                {  "Contact by email: ${user.email}" }
        user(roles = contains("ADMIN"), phone = !isNull) then
                { "Contact by phone: ${user.phone}" }
        user(roles = contains("ADMIN")) then
                { "Inform billing, user: ${user.name}" }
        otherwise { "no action" }
    }

fun main() {
    val c1 = contactMethod(User("Daniel", "DE", listOf("ADMIN"), phone = "123-456-789"))
    println(c1)
    val c2 = kopamaMethod(User("Daniel", "DE", listOf("ADMIN"), phone = "123-456-789"))
    println(c2)
}