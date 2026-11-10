package net.justmili.mcl.v2.lib.config.options

import net.justmili.mcl.v2.lib.codecs.EnumCodec
import java.awt.Color

interface OptionBounds<in O> {

    fun isValid(value: O): Boolean {
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

class RangeBounds<O : Comparable<O>>(private val min: O, private val max: O) : OptionBounds<O> {

    override fun isValid(value: O): Boolean {
        return value in min..max
    }

    override fun describe(): String {
        return "Allowed range: $min to $max"
    }
}

object RgbBounds : OptionBounds<Color> {

    override fun isValid(value: Color): Boolean {
        return value.alpha == 255
    }

    override fun describe(): String {
        return "Format: #RRGGBB"
    }
}

object ArgbBounds : OptionBounds<Color> {

    override fun describe(): String {
        return "Format: #AARRGGBB"
    }
}