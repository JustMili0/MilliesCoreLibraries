package net.justmili.mcl.v2.lib.config.options

import net.justmili.mcl.v2.lib.codec.EnumCodec
import java.awt.Color

interface OptionBounds<O> {
    fun clamp(value: O): O = value
    fun info(): String
    fun info(value: O): String? = null
}

object BooleanBounds : OptionBounds<Boolean> {
    override fun info() = "Allowed values: true, false"
}

class LengthBounds(private val max: Int) : OptionBounds<String> {
    override fun clamp(value: String) = value.take(max)
    override fun info() = "Max length: $max"
    override fun info(value: String) = "max=$max"
}

class RangeBounds<O : Comparable<O>>(private val min: O, private val max: O) : OptionBounds<O> {
    override fun clamp(value: O) = value.coerceIn(min, max)
    override fun info() = "Allowed range: $min to $max"
    override fun info(value: O) = "min=$min, max=$max"
}

class EnumBounds<E : Enum<E>>(private val enum: Class<E>) : OptionBounds<E> {
    override fun info() = "Allowed values: " + EnumCodec.names(enum)
    override fun info(value: E) = "other=" + enum.enumConstants.filter { it != value }.joinToString(", ", "[", "]") { EnumCodec.nameOf(it) }
}

object RgbBounds : OptionBounds<Color> {
    override fun clamp(value: Color) = if (value.alpha == 255) value else Color(value.rgb and 0xFFFFFF)
    override fun info() = "Format: RRGGBB"
    override fun info(value: Color) = "rgb=[${value.red}, ${value.green}, ${value.blue}]"
}

object ArgbBounds : OptionBounds<Color> {
    override fun info() = "Format: AARRGGBB"
    override fun info(value: Color) = "argb=[${value.alpha}, ${value.red}, ${value.green}, ${value.blue}]"
}

class UniqueBounds<E> : OptionBounds<List<E>> {
    override fun info() = "Entry duplicates aren't kept"
}