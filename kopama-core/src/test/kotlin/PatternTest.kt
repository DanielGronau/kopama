import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kopama.*
import java.io.Serializable

class PatternTest : StringSpec({
    val any = any<Int?>()
    val none = none<Int?>()
    val isNull = isNull<Int?>()
    val isEmpty = isEmpty<Int>()
    val isNotEmpty = isNotEmpty<Int>()

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

    "'isEmpty' should check if a collection is empty" {
        isEmpty(emptyList()) shouldBe true
        isEmpty(listOf(1, 2, 3)) shouldBe false
    }

    "'isNotEmpty' should check if a collection isn't empty" {
        isNotEmpty(listOf(1, 2, 3)) shouldBe true
        isNotEmpty(emptyList()) shouldBe false
    }

    "'hasSize' should check if a collection has the given size" {
        hasSize<Int>(0)(listOf()) shouldBe true
        hasSize<Int>(2)(listOf()) shouldBe false
        hasSize<Int>(2)(listOf(1)) shouldBe false
        hasSize<Int>(2)(listOf(1, 2)) shouldBe true
        hasSize<Int>(0)(listOf(1, 2)) shouldBe false
    }

    "'all' should check if a pattern matches for all elements" {
        all(isNull)(listOf()) shouldBe true
        all(isNull)(listOf(null)) shouldBe true
        all(isNull)(listOf(null, null)) shouldBe true
        all(isNull)(listOf(1)) shouldBe false
        all(isNull)(listOf(null, 1, null)) shouldBe false
    }

    "'any' should check if a pattern matches for at least one element" {
        any(isNull)(listOf()) shouldBe false
        any(isNull)(listOf(null)) shouldBe true
        any(isNull)(listOf(null, null)) shouldBe true
        any(isNull)(listOf(1)) shouldBe false
        any(isNull)(listOf(null, 1, null)) shouldBe true
        any(isNull)(listOf(1, 2, 3)) shouldBe false
    }

    "'none' should fail if a pattern matches for any element" {
        none(isNull)(listOf()) shouldBe true
        none(isNull)(listOf(null)) shouldBe false
        none(isNull)(listOf(null, null)) shouldBe false
        none(isNull)(listOf(1)) shouldBe true
        none(isNull)(listOf(null, 1, null)) shouldBe false
        none(isNull)(listOf(1, 2, 3)) shouldBe true
    }

    "'contains' should check if a collection contains a given value" {
        contains("d")(listOf()) shouldBe false
        contains("d")(listOf("a")) shouldBe false
        contains("d")(listOf("d")) shouldBe true
        contains("d")(listOf("d", "d")) shouldBe true
        contains("d")(listOf("a", "b", "c", "d")) shouldBe true
        contains("d")(listOf("a", "b", "c", "e")) shouldBe false
    }

    "'containsAll' should check if a collection contains all given values" {
        containsAll("a", "b")(listOf()) shouldBe false
        containsAll("a", "b")(listOf("a")) shouldBe false
        containsAll("a", "b")(listOf("a", "c", "d")) shouldBe false
        containsAll("a", "b")(listOf("b", "c", "a")) shouldBe true
    }

    "'containsAny' should check if a collection contains any of the given values" {
        containsAny("a", "b")(listOf()) shouldBe false
        containsAny("a", "b")(listOf("a")) shouldBe true
        containsAny("a", "b")(listOf("b", "c", "d")) shouldBe true
        containsAny("a", "b")(listOf("c", "d", "e")) shouldBe false
    }

    "'containsNone' should check if a collection contains any of the given values" {
        containsNone("a", "b")(listOf()) shouldBe true
        containsNone("a", "b")(listOf("a")) shouldBe false
        containsNone("a", "b")(listOf("b", "c", "d")) shouldBe false
        containsNone("a", "b")(listOf("c", "d", "e")) shouldBe true
    }

    "the indexed access should apply the given pattern at the indicated element" {
        eq(5)[2](listOf(1, 3, 5, 7)) shouldBe true
        eq(5)[2](listOf(5, 5, 6, 5)) shouldBe false
        eq(5)[0](listOf(5, 4, 2, 1)) shouldBe true
        eq(5)[3](listOf(1, 2, 4, 5)) shouldBe true
        eq(5)[2](listOf(1)) shouldBe false
        eq(5)[-2](listOf(1)) shouldBe false
    }

    // Map Patterns

    "'keys' should check if the given pattern matches the keys of a map" {
        keys<String, Int>(all(startsWith("a")))(mapOf("a" to 1, "ab" to 2)) shouldBe true
        keys<String, Int>(all(startsWith("a")))(mapOf("a" to 1, "b" to 2)) shouldBe false
    }

    "'values' should check if the given pattern matches the values of a map" {
        values<Int, String>(all(startsWith("a")))(mapOf(1 to "a", 2 to "ab")) shouldBe true
        values<Int, String>(all(startsWith("a")))(mapOf(1 to "a", 2 to "b")) shouldBe false
    }

    "'entries' should check if the given pattern matches the entries of a map" {
        entries(all(pair { a: Int, b: String -> a.toString() == b }))(mapOf(5 to "5", 7 to "7")) shouldBe true
        entries(all(pair { a: Int, b: String -> a.toString() == b }))(mapOf(5 to "5", 8 to "7")) shouldBe false
    }

    // Tuple Patterns

    "'pair' should check if the given condition for the elements of a pair holds" {
        pair { a: Int, b: String -> a.toString() == b }(5 to "5") shouldBe true
        pair { a: Int, b: String -> a.toString() == b }(5 to "6") shouldBe false
    }

    "'pair' should check if the given patterns for the elements of a pair hold" {
        pair(startsWith("a"), gt(5))("and" to 7) shouldBe true
        pair(startsWith("a"), gt(5))("or" to 7) shouldBe false
        pair(startsWith("a"), gt(5))("and" to 3) shouldBe false
        pair(startsWith("a"), gt(5))("or" to 3) shouldBe false
    }

    "'first' should check if the given pattern holds for the first element of a pair" {
        first<String, Int>(startsWith("a"))("and" to 7) shouldBe true
        first<String, Int>(startsWith("a"))("or" to 7) shouldBe false
    }

    "'second' should check if the given pattern holds for the second element of a pair" {
        second<String, Int>(gt(5))("and" to 7) shouldBe true
        second<String, Int>(gt(5))("and" to 3) shouldBe false
    }

    "'triple' should check if the given condition for the elements of a triple holds" {
        triple { a: Int, b: String, c: Int -> a.toString() == b && c > a }(Triple(5, "5", 7)) shouldBe true
        triple { a: Int, b: String, c: Int -> a.toString() == b && c > a }(Triple(5, "6", 7)) shouldBe false
    }

    "'triple' should check if the given patterns for the elements of a triple hold" {
        triple(startsWith("a"), gt(5), lt(10))(Triple("and", 7, 5)) shouldBe true
        triple(startsWith("a"), gt(5), lt(10))(Triple("or", 7, 5)) shouldBe false
        triple(startsWith("a"), gt(5), lt(10))(Triple("and", 3, 5)) shouldBe false
        triple(startsWith("a"), gt(5), lt(10))(Triple("and", 7, 13)) shouldBe false
        triple(startsWith("a"), gt(5), lt(10))(Triple("or", 3, 13)) shouldBe false
    }

    "'triple1' should check if the given pattern holds for the first element of a triple" {
        triple1<String, Int, Int>(startsWith("a"))(Triple("and", 7, 9)) shouldBe true
        triple1<String, Int, Int>(startsWith("a"))(Triple("or", 7, 9)) shouldBe false
    }

    "'triple2' should check if the given pattern holds for the second element of a triple" {
        triple2<Int, String, Int>(startsWith("a"))(Triple(1, "and", 9)) shouldBe true
        triple2<Int, String, Int>(startsWith("a"))(Triple(1, "or", 9)) shouldBe false
    }

    "'triple' should check if the given pattern holds for the third element of a triple" {
        triple3<Int, Int, String>(startsWith("a"))(Triple(1, 2, "and")) shouldBe true
        triple3<Int, Int, String>(startsWith("a"))(Triple(1, 3, "or")) shouldBe false
    }


})