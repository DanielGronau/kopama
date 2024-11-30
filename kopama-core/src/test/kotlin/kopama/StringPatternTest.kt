package kopama

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class StringPatternTest : StringSpec({

    "'hasToString' should check if calling toString() matches the given string" {
        hasToString<Int>("23")(23) shouldBe true
        hasToString<Int>("23")(15) shouldBe false
    }

    "'eqIgnoreCase' should check if a string is the same as the given one, ignoring upper- and lowercase" {
        eqIgnoreCase("")("") shouldBe true
        eqIgnoreCase("teStSTRinG")("TestString") shouldBe true
        eqIgnoreCase("teStSTRinG")("TestStríng") shouldBe false
    }

    "'startsWith' should check if a string starts with a given prefix" {
        startsWith("")("") shouldBe true
        startsWith("")("test") shouldBe true
        startsWith("test")("") shouldBe false
        startsWith("test")("test") shouldBe true
        startsWith("test")("testString") shouldBe true
        startsWith("testString")("test") shouldBe false
    }

    "'endsWith' should check if a string ends with a given suffix" {
        endsWith("")("") shouldBe true
        endsWith("")("test") shouldBe true
        endsWith("test")("") shouldBe false
        endsWith("test")("test") shouldBe true
        endsWith("String")("testString") shouldBe true
        endsWith("testString")("test") shouldBe false
    }

    "'containsString' should check if a string ends contains a given one" {
        containsString("")("") shouldBe true
        containsString("")("test") shouldBe true
        containsString("test")("") shouldBe false
        containsString("test")("test") shouldBe true
        containsString("test")("testStrings") shouldBe true
        containsString("String")("testStrings") shouldBe true
        containsString("estS")("testStrings") shouldBe true
        containsString("testString")("tests") shouldBe false
    }

    "'hasLength' should check if a string has the given length" {
        hasLength(3)("abc") shouldBe true
        hasLength(3)("") shouldBe false
        hasLength(3)("abcd") shouldBe false
    }

    "'regex' should check if a string matches the given regex" {
        regex("""^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$""")("test-mail@acme.com") shouldBe true
        regex("""^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$""")("test@mail@acme.com") shouldBe false
        regex("""^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$""")("testmail@acme") shouldBe false
    }
})