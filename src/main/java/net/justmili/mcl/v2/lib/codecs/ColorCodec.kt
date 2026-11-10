package net.justmili.mcl.v2.lib.codecs

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.justmili.mcl.v2.lib.util.common.Colors
import java.awt.Color

object ColorCodec {
    @JvmField val RGB: Codec<Color> = of(false)
    @JvmField val ARGB: Codec<Color> = of(true)

    @JvmStatic
    fun of(hasAlpha: Boolean): Codec<Color> {
        return Codec.STRING.comapFlatMap({ text ->
            val color = Colors.hexToColor(text, hasAlpha)
            if (color != null) DataResult.success(color) else DataResult.error { "'$text' is not a valid color, expected " + if (hasAlpha) "#AARRGGBB" else "#RRGGBB" }
        }, { color -> Colors.colorToHex(color, hasAlpha) })
    }
}