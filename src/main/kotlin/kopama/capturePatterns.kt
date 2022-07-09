package kopama

class Capture : Pattern {
    var value : Any? = ValueNotSet
        private set

    override fun test(obj: Any?) = true.also { value = obj }
}

object ValueNotSet

fun capture() = Capture()