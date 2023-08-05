import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.*
import java.io.Serializable

class PatternTest : StringSpec({
    val any = any<Int?>()
    val none = none<Int?>()
    val isNull = isNull<Int?>()

    // Constant Patterns

    "'any()' should match everything" {
        any(23) shouldBe true
        any(null) shouldBe true
    }

    "'none()' shouldn't match anything" {
        none(23) shouldBe false
        none(null) shouldBe false
    }

    // Operator Patterns

    "'!' should invert the result of a pattern" {
        !isNull(23) shouldBe true
        !isNull(null) shouldBe false
    }

    "'and' should only succeed if both pattern operands succeed" {
        (any and isNull)(null) shouldBe true
        (any and isNull)(23) shouldBe false
        (isNull and any)(23) shouldBe false
        (isNull and none)(23) shouldBe false
    }

    "'or' should succeed if any of the pattern operands succeed" {
        (any or isNull)(null) shouldBe true
        (any or isNull)(23) shouldBe true
        (isNull or any)(23) shouldBe true
        (isNull or none)(23) shouldBe false
    }

    "'xor' should succeed if exactly one of the pattern operands succeed" {
        (any xor isNull)(null) shouldBe false
        (any xor isNull)(23) shouldBe true
        (isNull xor any)(23) shouldBe true
        (isNull xor none)(23) shouldBe false
    }

    "'allOf' should succeed if all pattern arguments succeed" {
        allOf<Int?>()(23) shouldBe true
        allOf(any)(23) shouldBe true
        allOf(isNull)(23) shouldBe false
        allOf(any, isNull, any)(null) shouldBe true
        allOf(any, isNull, any)(23) shouldBe false
    }

    "'anyOf' should succeed of any of the pattern arguments succeeds" {
        anyOf<Int?>()(null) shouldBe false
        anyOf(any)(23) shouldBe true
        anyOf(any, isNull, none)(23) shouldBe true
        anyOf(none, isNull, none)(23) shouldBe false
    }

    "'noneOf' should succeed if none of the pattern arguments succeeds" {
        noneOf<Int?>()(23) shouldBe true
        noneOf(any)(23) shouldBe false
        noneOf(isNull)(23) shouldBe true
        noneOf(any, isNull, any)(null) shouldBe false
        noneOf(none, isNull, none)(23) shouldBe true
    }

    "'on' should extract a value and apply it to a pattern" {
        (isNull on { s: String? -> s?.toInt() })(null) shouldBe true
        (isNull on { s: String? -> s?.toInt() })("23") shouldBe false
    }

    // Comparing Patterns

    "'isNull' should check if a value is null" {
        isNull(null) shouldBe true
        isNull(23) shouldBe false
    }

    "'eq' should check if a value is equal to a given one" {
        eq(23)(23) shouldBe true
        eq(23)(15) shouldBe false
        eq<Int?>(null)(null) shouldBe true
        eq<Int?>(null)(23) shouldBe false
    }

    "'isSame' should check if a value is the same instance as the given one" {
        class Test
        val t = Test()
        isSame(t)(t) shouldBe true
        isSame(t)(Test()) shouldBe false
    }

    "'oneOf' should check if a value is equal to one of the given ones" {
        oneOf<Int?>()(23) shouldBe false
        oneOf(23)(23) shouldBe true
        oneOf(23)(15) shouldBe false
        oneOf(23, 2, 7, 15, 1234)(15) shouldBe true
        oneOf(23, 2, 7, 16, 1234)(15) shouldBe false
    }

    "'isA' should check if a value is an instance of the given class or interface" {
        isA<String>()("foo") shouldBe true
        isA<CharSequence>()("foo") shouldBe true
        isA<Serializable>()("foo") shouldBe true
        isA<String>()(23) shouldBe false
    }

    "'gt' should check if a comparable value is greater than a given one" {
        gt(15)(23) shouldBe true
        gt(15)(15) shouldBe false
        gt(15)(3) shouldBe false
    }

    "'ge' should check if a comparable value is greater than or equal to a given one" {
        ge(15)(23) shouldBe true
        ge(15)(15) shouldBe true
        ge(15)(3) shouldBe false
    }

    "'lt' should check if a comparable value is less than a given one" {
        lt(15)(23) shouldBe false
        lt(15)(15) shouldBe false
        lt(15)(3) shouldBe true
    }

    "'le' should check if a comparable value is less than or equal to a given one" {
        le(15)(23) shouldBe false
        le(15)(15) shouldBe true
        le(15)(3) shouldBe true
    }

    // String Patterns

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

    "'regex' should check if a string matches the given regex" {
        regex("""^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$""")("test-mail@acme.com") shouldBe true
        regex("""^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$""")("test@mail@acme.com") shouldBe false
        regex("""^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$""")("testmail@acme") shouldBe false
    }


})