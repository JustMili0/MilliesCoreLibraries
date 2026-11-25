package net.justmili.mcl.v2.lib.config.options

import com.google.gson.JsonPrimitive
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import net.justmili.mcl.v2.lib.codec.*
import net.justmili.mcl.v2.lib.config.MConfigLoader
import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.config.serializers.ConfigIO
import net.justmili.mcl.v2.lib.util.common.Colors
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.ExtraCodecs
import net.minecraft.util.ExtraCodecs.TagOrElementLocation
import java.awt.Color

abstract class OptionBuilder<O : Any, B : OptionBuilder<O, B>>(protected val loader: MConfigLoader, private val category: CategoryItem, protected val key: String, protected val codec: Codec<O>) {
    private var defaultValue: O? = null
    protected var flagId = 0
    private var attachedImage: ResourceLocation? = null
    private var comment: String? = null
    private var oldKey: String? = null

    @Suppress("UNCHECKED_CAST")
    protected fun self() = this as B

    protected fun setDefaultValue(value: O): B {
        this.defaultValue = value
        return self()
    }

    protected open fun getLimit(): OptionBounds<O>? = null

    protected open fun isText() = false

    protected open fun create(value: O, limit: OptionBounds<O>?) = ConfigOption(key, value, codec, limit, flagId, isText(), loader)

    fun addFlags(vararg flags: OptionFlags): B {
        for (flag in flags) {
            flagId = flagId or flag.id
        }
        return self()
    }

    fun addImage(path: ResourceLocation): B {
        this.attachedImage = path
        return self()
    }

    fun addComment(comment: String): B {
        this.comment = ConfigIO.joinComments(this.comment, comment)
        return self()
    }

    fun tryGetOldConfigKeyValue(oldKey: String): B {
        require(oldKey.isNotBlank()) { "Old key of config option '$key' can't be blank." }
        this.oldKey = oldKey
        return self()
    }

    open fun buildOption(): ConfigOption<O> {
        val value = checkNotNull(defaultValue) { "Config option '$key' has no default value." }
        val limit = getLimit()
        require(limit == null || limit.clamp(value) == value) { "Default value '$value' of config option '$key' does not meet its own limits." }

        val option = create(value, limit)
        option.oldKey = oldKey
        option.attachedImage = attachedImage
        option.comment = comment

        category.add(option)
        return option
    }
}

class CodecOptionBuilder<O : Any>(loader: MConfigLoader, cat: CategoryItem, key: String, codec: Codec<O>) : OptionBuilder<O, CodecOptionBuilder<O>>(loader, cat, key, codec) {
    fun setDefault(value: O) = setDefaultValue(value)
}

class BooleanOptionBuilder(loader: MConfigLoader, cat: CategoryItem, key: String) : OptionBuilder<Boolean, BooleanOptionBuilder>(loader, cat, key, Codec.BOOL) {
    override fun getLimit() = BooleanBounds
    fun setDefault(value: Boolean) = setDefaultValue(value)
}

class StringOptionBuilder(loader: MConfigLoader, cat: CategoryItem, key: String) : OptionBuilder<String, StringOptionBuilder>(loader, cat, key, Codec.STRING) {
    private var charLimit = Int.MAX_VALUE

    // Doesn't need to override isText
    override fun getLimit() = if (charLimit == Int.MAX_VALUE) null else LengthBounds(charLimit)
    fun setDefault(value: String) = setDefaultValue(value)

    fun setCharLimit(limit: Int): StringOptionBuilder {
        require(limit >= 0) { "Character limit can't be negative." }
        this.charLimit = limit
        return this
    }
}

abstract class NumberOptionBuilder<O, B : NumberOptionBuilder<O, B>>(loader: MConfigLoader, cat: CategoryItem, key: String, codec: Codec<O>) : OptionBuilder<O, B>(loader, cat, key, codec) where O : Comparable<O>, O : Any {
    private var range: RangeBounds<O>? = null

    override fun getLimit() = range

    protected fun setRangeLimit(min: O, max: O): B {
        require(min <= max) { "Minimum can't be higher than maximum." }
        this.range = RangeBounds(min, max)
        return self()
    }
}

class IntOptionBuilder(loader: MConfigLoader, cat: CategoryItem, key: String) : NumberOptionBuilder<Int, IntOptionBuilder>(loader, cat, key, Codec.INT) {
    fun setDefault(value: Int) = setDefaultValue(value)
    fun setRange(min: Int, max: Int) = setRangeLimit(min, max)
}

class LongOptionBuilder(loader: MConfigLoader, cat: CategoryItem, key: String) : NumberOptionBuilder<Long, LongOptionBuilder>(loader, cat, key, Codec.LONG) {
    fun setDefault(value: Long) = setDefaultValue(value)
    fun setRange(min: Long, max: Long) = setRangeLimit(min, max)
}

class FloatOptionBuilder(loader: MConfigLoader, cat: CategoryItem, key: String) : NumberOptionBuilder<Float, FloatOptionBuilder>(loader, cat, key, Codec.FLOAT) {
    fun setDefault(value: Float) = setDefaultValue(value)
    fun setRange(min: Float, max: Float) = setRangeLimit(min, max)
}

class DoubleOptionBuilder(loader: MConfigLoader, cat: CategoryItem, key: String) : NumberOptionBuilder<Double, DoubleOptionBuilder>(loader, cat, key, Codec.DOUBLE) {
    fun setDefault(value: Double) = setDefaultValue(value)
    fun setRange(min: Double, max: Double) = setRangeLimit(min, max)
}

class EnumOptionBuilder<E : Enum<E>>(loader: MConfigLoader, cat: CategoryItem, key: String, private val enum: Class<E>) : OptionBuilder<E, EnumOptionBuilder<E>>(loader, cat, key, EnumCodec.of(enum)) {
    override fun isText() = true
    override fun getLimit() = EnumBounds(enum)
    fun setDefault(value: E) = setDefaultValue(value)
}

abstract class ColorOptionBuilder<B : ColorOptionBuilder<B>>(loader: MConfigLoader, cat: CategoryItem, key: String, private val hasAlpha: Boolean) : OptionBuilder<Color, B>(loader, cat, key, if (hasAlpha) ColorCodec.ARGB_HEX else ColorCodec.RGB_HEX) {
    override fun isText() = true
    override fun getLimit() = if (hasAlpha) ArgbBounds else RgbBounds
    protected fun setColor(color: Color) = setDefaultValue(color)
    protected fun parseHex(hex: String) = Colors.hexToColor(hex, hasAlpha)
}

class RgbOptionBuilder(loader: MConfigLoader, cat: CategoryItem, key: String) : ColorOptionBuilder<RgbOptionBuilder>(loader, cat, key, false) {
    fun setDefault(hex: String) = setColor(parseHex(hex))
    fun setDefault(rgb: Int) = setColor(Color(rgb))
    fun setDefault(r: Float, g: Float, b: Float) = setColor(Color(r, g, b))
}

class ArgbOptionBuilder(loader: MConfigLoader, cat: CategoryItem, key: String) : ColorOptionBuilder<ArgbOptionBuilder>(loader, cat, key, true) {
    fun setDefault(hex: String) = setColor(parseHex(hex))
    fun setDefault(argb: Int) = setColor(Color(argb, true))
    fun setDefault(a: Float, r: Float, g: Float, b: Float) = setColor(Color(r, g, b, a))
}

class ListOptionBuilder<E : Any>(loader: MConfigLoader, cat: CategoryItem, key: String, codec: Codec<E>, private val collapse: Boolean) : OptionBuilder<@JvmSuppressWildcards List<E>, ListOptionBuilder<E>>(loader, cat, key, ListCodec.of(codec, collapse)) {
    override fun getLimit() = if (collapse) UniqueBounds<E>() else null
    override fun create(value: List<E>, limit: OptionBounds<List<E>>?) = ListConfigOption(key, ListCodec.normalize(value, collapse), codec, limit, flagId, loader, collapse)
    override fun buildOption() = super.buildOption() as ListConfigOption<E>
    fun setDefault(vararg elements: E) = setDefaultValue(elements.asList())
}

class HolderSetOptionBuilder<T : Any>(loader: MConfigLoader, cat: CategoryItem, key: String, private val registry: Registry<T>) : OptionBuilder<@JvmSuppressWildcards List<TagOrElementLocation>, HolderSetOptionBuilder<T>>(loader, cat, key, ListCodec.of(ExtraCodecs.TAG_OR_ELEMENT_ID, true)) {
    private val defaults = ArrayList<TagOrElementLocation>()

    override fun getLimit() = UniqueBounds<TagOrElementLocation>()
    override fun create(value: List<TagOrElementLocation>, limit: OptionBounds<List<TagOrElementLocation>>?) = HolderSetConfigOption(key, ListCodec.normalize(value, true), codec, limit, flagId, loader, registry)

    override fun buildOption(): HolderSetConfigOption<T> {
        setDefaultValue(defaults.toList())
        @Suppress("UNCHECKED_CAST")
        return super.buildOption() as HolderSetConfigOption<T>
    }

    fun addDefault(vararg values: T): HolderSetOptionBuilder<T> {
        for (value in values) {
            val id = checkNotNull(registry.getKey(value)) { "Default value '$value' of option '$key' is not in registry '${registry.key()}'." }
            defaults.add(TagOrElementLocation(id, false))
        }
        return this
    }

    @SafeVarargs
    fun addDefault(vararg tags: TagKey<T>): HolderSetOptionBuilder<T> {
        for (tag in tags) defaults.add(TagOrElementLocation(tag.location(), true))
        return this
    }

    fun addDefault(vararg ids: ResourceLocation): HolderSetOptionBuilder<T> {
        for (id in ids) defaults.add(TagOrElementLocation(id, false))
        return this
    }

    fun addDefault(vararg entries: String): HolderSetOptionBuilder<T> {
        for (entry in entries) {
            defaults.add(ExtraCodecs.TAG_OR_ELEMENT_ID.parse(JsonOps.INSTANCE, JsonPrimitive(entry)).result().orElseThrow {
                IllegalArgumentException("Default value '$entry' of option '$key' is not a valid id or tag.")
            })
        }
        return this
    }
}