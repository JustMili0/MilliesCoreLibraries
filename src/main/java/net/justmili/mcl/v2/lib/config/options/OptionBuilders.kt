package net.justmili.mcl.v2.lib.config.options

import com.mojang.serialization.Codec
import net.justmili.mcl.v2.lib.codecs.ColorCodec
import net.justmili.mcl.v2.lib.codecs.EnumCodec
import net.justmili.mcl.v2.lib.config.MConfigLoader
import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.util.common.Colors
import net.minecraft.resources.ResourceLocation
import java.awt.Color

abstract class OptionBuilder<T : Any, B : OptionBuilder<T, B>>(private val loader: MConfigLoader, private val category: CategoryItem, protected val key: String, private var codec: Codec<T>) {
    private var defaultValue: T? = null
    private var flagMask = 0
    private var attachedImage: ResourceLocation? = null
    private var comment: String? = null
    private var oldKey: String? = null

    @Suppress("UNCHECKED_CAST")
    protected fun self(): B {
        return this as B
    }

    protected fun setDefaultValue(value: T): B {
        this.defaultValue = value
        return self()
    }

    protected fun setCodec(codec: Codec<T>) {
        this.codec = codec
    }

    protected open fun getLimit(): OptionBounds<T>? {
        return null
    }

    protected open fun isText(): Boolean {
        return false
    }

    fun addFlags(vararg flags: OptionFlags): B {
        for (flag in flags) {
            flagMask = flagMask or flag.mask
        }
        return self()
    }

    fun addImage(path: ResourceLocation): B {
        this.attachedImage = path
        return self()
    }

    fun addComment(comment: String): B {
        this.comment = this.comment?.let { it + "\n" + comment } ?: comment
        return self()
    }

    fun tryGetOldConfigKeyValue(oldKey: String): B {
        require(oldKey.isNotBlank()) { "Old key of config option '$key' can't be blank." }
        this.oldKey = oldKey
        return self()
    }

    fun buildOption(): ConfigOption<T> {
        val value = checkNotNull(defaultValue) { "Config option '$key' has no default value." }
        val limit = getLimit()
        require(limit == null || limit.isValid(value)) { "Default value '$value' of config option '$key' does not meet its own limits." }

        val option = ConfigOption(key, value, codec, limit, loader, flagMask, isText())
        option.oldKey = oldKey
        option.attachedImage = attachedImage
        option.comment = comment

        category.add(option)
        return option
    }
}

class CodecOptionBuilder<T : Any>(loader: MConfigLoader, category: CategoryItem, key: String, codec: Codec<T>) : OptionBuilder<T, CodecOptionBuilder<T>>(loader, category, key, codec) {

    fun setDefault(value: T): CodecOptionBuilder<T> {
        return setDefaultValue(value)
    }
}

class BooleanOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : OptionBuilder<Boolean, BooleanOptionBuilder>(loader, category, key, Codec.BOOL) {

    override fun getLimit(): OptionBounds<Boolean> {
        return BooleanBounds
    }

    fun setDefault(value: Boolean): BooleanOptionBuilder {
        return setDefaultValue(value)
    }
}

class StringOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : OptionBuilder<String, StringOptionBuilder>(loader, category, key, Codec.STRING) {
    private var charLimit = Int.MAX_VALUE

    // Doesn't need to override isText

    override fun getLimit(): OptionBounds<String>? {
        return if (charLimit == Int.MAX_VALUE) null else LengthBounds(charLimit)
    }

    fun setDefault(value: String): StringOptionBuilder {
        return setDefaultValue(value)
    }

    fun setCharLimit(limit: Int): StringOptionBuilder {
        require(limit >= 0) { "Character limit can't be negative." }
        this.charLimit = limit
        return this
    }
}

abstract class NumberOptionBuilder<T, B : NumberOptionBuilder<T, B>>(loader: MConfigLoader, category: CategoryItem, key: String, codec: Codec<T>) : OptionBuilder<T, B>(loader, category, key, codec) where T : Comparable<T>, T : Any {
    private var range: RangeBounds<T>? = null

    override fun getLimit(): OptionBounds<T>? {
        return range
    }

    protected fun setRangeLimit(min: T, max: T): B {
        require(min <= max) { "Minimum can't be higher than maximum." }
        this.range = RangeBounds(min, max)
        return self()
    }
}

class IntOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : NumberOptionBuilder<Int, IntOptionBuilder>(loader, category, key, Codec.INT) {

    fun setDefault(value: Int): IntOptionBuilder {
        return setDefaultValue(value)
    }

    fun setRange(min: Int, max: Int): IntOptionBuilder {
        return setRangeLimit(min, max)
    }
}

class LongOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : NumberOptionBuilder<Long, LongOptionBuilder>(loader, category, key, Codec.LONG) {

    fun setDefault(value: Long): LongOptionBuilder {
        return setDefaultValue(value)
    }

    fun setRange(min: Long, max: Long): LongOptionBuilder {
        return setRangeLimit(min, max)
    }
}

class FloatOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : NumberOptionBuilder<Float, FloatOptionBuilder>(loader, category, key, Codec.FLOAT) {

    fun setDefault(value: Float): FloatOptionBuilder {
        return setDefaultValue(value)
    }

    fun setRange(min: Float, max: Float): FloatOptionBuilder {
        return setRangeLimit(min, max)
    }
}

class DoubleOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : NumberOptionBuilder<Double, DoubleOptionBuilder>(loader, category, key, Codec.DOUBLE) {

    fun setDefault(value: Double): DoubleOptionBuilder {
        return setDefaultValue(value)
    }

    fun setRange(min: Double, max: Double): DoubleOptionBuilder {
        return setRangeLimit(min, max)
    }
}

class EnumOptionBuilder<E : Enum<E>>(loader: MConfigLoader, category: CategoryItem, key: String, private val type: Class<E>) : OptionBuilder<E, EnumOptionBuilder<E>>(loader, category, key, EnumCodec.of(type)) {

    override fun isText(): Boolean {
        return true
    }

    override fun getLimit(): OptionBounds<E> {
        return EnumBounds(type)
    }

    fun setDefault(value: E): EnumOptionBuilder<E> {
        return setDefaultValue(value)
    }
}

class ColorOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : OptionBuilder<Color, ColorOptionBuilder>(loader, category, key, ColorCodec.RGB) {
    private var hasAlpha = false

    override fun isText(): Boolean {
        return true
    }

    override fun getLimit(): OptionBounds<Color> {
        return if (hasAlpha) ArgbBounds else RgbBounds
    }

    fun setDefaultRgb(hex: String): ColorOptionBuilder {
        return setColor(parseHex(hex, false), false)
    }

    fun setDefaultRgb(rgb: Int): ColorOptionBuilder {
        return setColor(Colors.intToColor(rgb, false), false)
    }

    fun setDefaultRgb(r: Float, g: Float, b: Float): ColorOptionBuilder {
        return setColor(Colors.floatToColor(r, g, b), false)
    }

    fun setDefaultArgb(hex: String): ColorOptionBuilder {
        return setColor(parseHex(hex, true), true)
    }

    fun setDefaultArgb(argb: Int): ColorOptionBuilder {
        return setColor(Colors.intToColor(argb, true), true)
    }

    fun setDefaultArgb(a: Float, r: Float, g: Float, b: Float): ColorOptionBuilder {
        return setColor(Colors.floatToColor(a, r, g, b), true)
    }

    private fun setColor(color: Color, hasAlpha: Boolean): ColorOptionBuilder {
        this.hasAlpha = hasAlpha
        setCodec(if (hasAlpha) ColorCodec.ARGB else ColorCodec.RGB)
        return setDefaultValue(color)
    }

    private fun parseHex(hex: String, hasAlpha: Boolean): Color {
        return requireNotNull(Colors.hexToColor(hex, hasAlpha)) { "Default color '$hex' of config option '$key' is not a valid hex color." }
    }
}