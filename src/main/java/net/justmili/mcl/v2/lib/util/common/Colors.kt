package net.justmili.mcl.v2.lib.util.common

import java.awt.Color

object Colors {
    private const val HEX = "0123456789ABCDEF"
    private const val OPAQUE = 0xFF000000.toInt()
    private const val INVALID = 0xFFFF00DC.toInt()

    @JvmStatic
    fun colorToInt(color: Color, hasAlpha: Boolean = false): Int {
        return if (hasAlpha) color.rgb else color.rgb or OPAQUE
    }

    @JvmStatic
    fun colorToHex(color: Color, hasAlpha: Boolean = false): String {
        return intToHex(color.rgb, hasAlpha)
    }

    @JvmStatic
    fun floatToColor(a: Float, r: Float, g: Float, b: Float): Color {
        return Color(r, g, b, a)
    }

    @JvmStatic
    fun floatToColor(r: Float, g: Float, b: Float): Color {
        return Color(r, g, b)
    }

    @JvmStatic
    fun floatToInt(a: Float, r: Float, g: Float, b: Float): Int {
        return (channel(a, "Alpha") shl 24) or (channel(r, "Red") shl 16) or (channel(g, "Green") shl 8) or channel(b, "Blue")
    }

    @JvmStatic
    fun floatToInt(r: Float, g: Float, b: Float): Int {
        return floatToInt(1f, r, g, b)
    }

    @JvmStatic
    fun floatToHex(a: Float, r: Float, g: Float, b: Float): String {
        return intToHex(floatToInt(a, r, g, b), true)
    }

    @JvmStatic
    fun floatToHex(r: Float, g: Float, b: Float): String {
        return intToHex(floatToInt(r, g, b), false)
    }

    @JvmStatic
    fun intToColor(value: Int, hasAlpha: Boolean = false): Color {
        return Color(value, hasAlpha)
    }

    @JvmStatic
    fun intToHex(value: Int, hasAlpha: Boolean = false): String {
        val digits = if (hasAlpha) 8 else 6
        val chars = CharArray(digits)
        for (i in 0 until digits) {
            chars[i] = HEX[(value ushr ((digits - 1 - i) * 4)) and 15]
        }
        return String(chars)
    }

    @JvmStatic
    fun hexToColor(text: String, hasAlpha: Boolean = false): Color {
        return intToColor(hexToInt(text, hasAlpha), hasAlpha)
    }

    @JvmStatic
    fun hexToInt(text: String, hasAlpha: Boolean = false): Int {
        var start = 0
        var end = text.length
        while (start < end && text[start].isWhitespace()) start++
        while (end > start && text[end - 1].isWhitespace()) end--

        if (start < end && text[start] == '#') {
            start++
        } else if (end - start > 2 && text[start] == '0' && (text[start + 1] == 'x' || text[start + 1] == 'X')) {
            start += 2
        }

        val length = end - start
        if (length != 6 && length != 8) return INVALID

        var value = 0
        for (i in start until end) {
            val digit = hexDigit(text[i])
            if (digit < 0) return INVALID
            value = (value shl 4) or digit
        }
        return if (hasAlpha && length == 8) value else value or OPAQUE
    }

    private fun channel(value: Float, name: String): Int {
        require(value in 0f..1f) { "Color parameter outside of expected range: $name" }
        return (value * 255 + 0.5).toInt()
    }

    private fun hexDigit(c: Char): Int {
        return when (c) {
            in '0'..'9' -> c - '0'
            in 'a'..'f' -> c - 'a' + 10
            in 'A'..'F' -> c - 'A' + 10
            else -> -1
        }
    }
}