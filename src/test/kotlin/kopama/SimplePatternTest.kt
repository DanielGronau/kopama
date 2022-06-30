package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DataClassTest : StringSpec ({

     "isNull should test for null values" {
         val s = null
         when(match(s)) {
             eq("Alice") -> 1
             eq("Bob") -> 2
             isNull -> 3
             eq("Darius") -> 4
             else -> 5
         } shouldBe 3
     }

     "eq should test equality" {
         val s = "Cesar"
         when(match(s)) {
             eq("Alice") -> 1
             eq("Bob") -> 2
             eq("Cesar") -> 3
             eq("Darius") -> 4
             else -> 5
         } shouldBe 3
     }

    "any should match anything" {
        val s = "Cesar"
        when(match(s)) {
            eq("Alice") -> 1
            eq("Bob") -> 2
            any -> 3
            eq("Darius") -> 4
            else -> 5
        } shouldBe 3
    }

    "'!' should negate the pattern result" {
        val s = "Cesar"
        when(match(s)) {
            !any -> 1
            eq("Alice") -> 1
            !eq("Alice") -> 2
            else -> 3
        } shouldBe 2
    }

    "oneOf should match when one of the values is equal" {
        val s = "Cesar"
        when(match(s)) {
            oneOf("Alice", "Bob") -> 1
            oneOf("Cesar", "Darius") -> 2
            else -> 3
        } shouldBe 2
    }

    "or should match when one of the patterns matches" {
        val s = "Cesar"
        when(match(s)) {
            eq("Alice") or eq("Bob") -> 1
            eq("Cesar") or !any -> 2
            else -> 3
        } shouldBe 2
    }

    "and should match when both of the patterns match" {
        val s = "Cesar"
        when(match(s)) {
            eq("Alice") and eq("Cesar") -> 1
            eq("Cesar") and any -> 2
            else -> 3
        } shouldBe 2
    }

    "the range operator should deconstruct the object" {
        val s = Person("Roy", "Batty", 4)
        when(match(s)) {
            eq("Roy") .. eq("Black") .. eq(4) -> 1
            eq("Roy") .. eq("Batty") .. eq(4) -> 2
            else -> 3
        } shouldBe 2
    }

})

data class Person(val firstName: String, val lastName: String, val age: Int)