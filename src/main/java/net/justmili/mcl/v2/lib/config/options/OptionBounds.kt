package net.justmili.mcl.v2.lib.config.options

import net.justmili.mcl.v2.lib.codecs.EnumCodec
import java.awt.Color

interface OptionBounds<in O> {
    fun isValid(value: O) = true
    fun describe(): String
}

object BooleanBounds : OptionBounds<Boolean> {
    override fun describe() = "Allowed values: true, false"
}

class LengthBounds(private val max: Int) : OptionBounds<String> {
    override fun isValid(value: String) = value.length <= max
    override fun describe() = "Max length: $max"
}

class RangeBounds<O : Comparable<O>>(private val min: O, private val max: O) : OptionBounds<O> {
    override fun isValid(value: O) = value in min..max
    override fun describe() = "Allowed range: $min to $max"
}

class EnumBounds(private val type: Class<out Enum<*>>) : OptionBounds<Any> {
    override fun describe() = "Allowed values: " + EnumCodec.names(type)
}

object RgbBounds : OptionBounds<Color> {
    override fun isValid(value: Color) = value.alpha == 255
    override fun describe() = "Format: #RRGGBB"
}

object ArgbBounds : OptionBounds<Color> {
    override fun describe() = "Format: #AARRGGBB"
}

object UniqueBounds : OptionBounds<Any> {
    override fun describe() = "No duplicates"
}