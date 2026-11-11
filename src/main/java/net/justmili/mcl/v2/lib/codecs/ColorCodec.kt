package net.justmili.mcl.v2.lib.codecs

import com.mojang.serialization.Codec
import net.justmili.mcl.v2.lib.util.common.Colors
import java.awt.Color

object ColorCodec {
    @JvmField val RGB_HEX: Codec<Color> = ofHex(false)
    @JvmField val ARGB_HEX: Codec<Color> = ofHex(true)
    @JvmField val RGB_INT: Codec<Color> = ofInt(false)
    @JvmField val ARGB_INT: Codec<Color> = ofInt(true)

    @JvmStatic
    fun ofHex(hasAlpha: Boolean): Codec<Color> {
        return Codec.STRING.xmap({ text -> Colors.hexToColor(text, hasAlpha) }, { color -> Colors.colorToHex(color, hasAlpha) })
    }

    @JvmStatic
    fun ofInt(hasAlpha: Boolean): Codec<Color> {
        return Codec.INT.xmap({ value -> Colors.intToColor(value, hasAlpha) }, { color -> Colors.colorToInt(color, hasAlpha) })
    }
}