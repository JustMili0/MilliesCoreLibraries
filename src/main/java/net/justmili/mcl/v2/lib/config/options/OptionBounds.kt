package net.justmili.mcl.v2.lib.config.options

import net.justmili.mcl.v2.lib.config.codecs.EnumCodec

interface OptionBounds<in T> {

    fun isValid(value: T): Boolean {
        return true
    }

    fun describe(): String
}

object BooleanBounds : OptionBounds<Boolean> {

    override fun describe(): String {
        return "Allowed values: true, false"
    }
}

class EnumBounds(private val type: Class<out Enum<*>>) : OptionBounds<Any> {

    override fun describe(): String {
        return "Allowed values: " + EnumCodec.names(type)
    }
}

class LengthBounds(private val max: Int) : OptionBounds<String> {

    override fun isValid(value: String): Boolean {
        return value.length <= max
    }

    override fun describe(): String {
        return "Max length: $max"
    }
}

class RangeBounds<T : Comparable<T>>(private val min: T, private val max: T) : OptionBounds<T> {

    override fun isValid(value: T): Boolean {
        return value in min..max
    }

    override fun describe(): String {
        return "Allowed range: $min to $max"
    }
}
