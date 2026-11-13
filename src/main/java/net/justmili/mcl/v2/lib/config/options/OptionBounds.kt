package net.justmili.mcl.v2.lib.config.options

import net.justmili.mcl.v2.lib.codecs.EnumCodec
import java.awt.Color

interface OptionBounds<O> {
    fun clamp(value: O): O = value
    fun describe(): String
}

object BooleanBounds : OptionBounds<Boolean> {
    override fun describe() = "Allowed values: true, false"
}

class LengthBounds(private val max: Int) : OptionBounds<String> {
    override fun clamp(value: String) = value.take(max)
    override fun describe() = "Max length: $max"
}

class RangeBounds<O : Comparable<O>>(private val min: O, private val max: O) : OptionBounds<O> {
    override fun clamp(value: O) = value.coerceIn(min, max)
    override fun describe() = "Allowed range: $min to $max"
}

class EnumBounds<E : Enum<E>>(private val enum: Class<E>) : OptionBounds<E> {
    override fun describe() = "Allowed values: " + EnumCodec.names(enum)
}

object RgbBounds : OptionBounds<Color> {
    override fun clamp(value: Color) = if (value.alpha == 255) value else Color(value.rgb and 0xFFFFFF)
    override fun describe() = "Format: RRGGBB"
}

object ArgbBounds : OptionBounds<Color> {
    override fun describe() = "Format: AARRGGBB"
}

class UniqueBounds<E> : OptionBounds<List<E>> {
    override fun describe() = "Entry duplicates aren't kept"
}