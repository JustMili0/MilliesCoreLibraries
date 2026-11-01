package net.justmili.mcl.v2.lib.config.options

import com.mojang.serialization.Codec
import net.justmili.mcl.v2.lib.config.MConfigLoader
import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.minecraft.resources.ResourceLocation
import java.util.function.Predicate

abstract class OptionBuilder<T : Any, B : OptionBuilder<T, B>>(private val loader: MConfigLoader, private val category: CategoryItem, private val key: String, private val codec: Codec<T>) {
    private var defaultValue: T? = null
    private var flags: Array<out OptionFlags> = emptyArray()
    private var attachedImage: ResourceLocation? = null
    private var comment: String? = null

    protected abstract fun self(): B

    protected open fun getValidator(): Predicate<T> {
        return Predicate {true}
    }

    protected open fun getBounds(): String? {
        return null
    }

    protected fun setDefaultValue(value: T): B {
        this.defaultValue = value
        return self()
    }

    fun setFlags(vararg flags: OptionFlags): B {
        this.flags = flags
        return self()
    }

    fun getFlags(): Array<out OptionFlags> {
        return flags
    }

    fun addImage(path: ResourceLocation): B {
        this.attachedImage = path
        return self()
    }

    fun getImage(): ResourceLocation? {
        return attachedImage
    }

    fun setComment(comment: String): B {
        this.comment = comment
        return self()
    }

    fun buildOption(): ConfigOption<T> {
        val value = checkNotNull(defaultValue) {"Config option '$key' has no default value."}
        val validator = getValidator()
        require(validator.test(value)) {"Default value '$value' of config option '$key' does not meet its own limits."}

        val option = ConfigOption(key, value, codec, validator, getBounds(), loader::save, *flags)
        val image = attachedImage
        if (image != null) option.image = image

        val text = comment
        if (text != null) option.comment = text

        category.add(option)
        return option
    }
}

class BooleanOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : OptionBuilder<Boolean, BooleanOptionBuilder>(loader, category, key, Codec.BOOL) {

    override fun self(): BooleanOptionBuilder {
        return this
    }

    override fun getBounds(): String {
        return "Allowed values: true, false"
    }

    fun setDefault(value: Boolean): BooleanOptionBuilder {
        return setDefaultValue(value)
    }
}

class StringOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : OptionBuilder<String, StringOptionBuilder>(loader, category, key, Codec.STRING) {
    private var charLimit = Int.MAX_VALUE

    override fun self(): StringOptionBuilder {
        return this
    }

    override fun getValidator(): Predicate<String> {
        val limit = charLimit
        return Predicate {candidate: String -> candidate.length <= limit}
    }

    override fun getBounds(): String? {
        return if (charLimit == Int.MAX_VALUE) null else "Maximum length: $charLimit"
    }

    fun setDefault(value: String): StringOptionBuilder {
        return setDefaultValue(value)
    }

    fun setCharLimit(limit: Int): StringOptionBuilder {
        require(limit >= 0) {"Character limit can't be negative."}
        this.charLimit = limit
        return this
    }
}

abstract class NumberOptionBuilder<T, B : NumberOptionBuilder<T, B>>(loader: MConfigLoader, category: CategoryItem, key: String, codec: Codec<T>) : OptionBuilder<T, B>(loader, category, key, codec) where T : Comparable<T>, T : Any {
    private var range: ClosedRange<T>? = null

    override fun getValidator(): Predicate<T> {
        val limit = range
        return Predicate {candidate: T -> limit == null || candidate in limit}
    }

    override fun getBounds(): String? {
        val limit = range
        return if (limit == null) null else "Allowed range: ${limit.start} to ${limit.endInclusive}"
    }

    protected fun setRangeLimit(min: T, max: T): B {
        require(min <= max) {"Minimum can't be higher than maximum."}
        this.range = min..max
        return self()
    }
}

class IntOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : NumberOptionBuilder<Int, IntOptionBuilder>(loader, category, key, Codec.INT) {

    override fun self(): IntOptionBuilder {
        return this
    }

    fun setDefault(value: Int): IntOptionBuilder {
        return setDefaultValue(value)
    }

    fun setRange(min: Int, max: Int): IntOptionBuilder {
        return setRangeLimit(min, max)
    }
}

class LongOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : NumberOptionBuilder<Long, LongOptionBuilder>(loader, category, key, Codec.LONG) {

    override fun self(): LongOptionBuilder {
        return this
    }

    fun setDefault(value: Long): LongOptionBuilder {
        return setDefaultValue(value)
    }

    fun setRange(min: Long, max: Long): LongOptionBuilder {
        return setRangeLimit(min, max)
    }
}

class FloatOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : NumberOptionBuilder<Float, FloatOptionBuilder>(loader, category, key, Codec.FLOAT) {

    override fun self(): FloatOptionBuilder {
        return this
    }

    fun setDefault(value: Float): FloatOptionBuilder {
        return setDefaultValue(value)
    }

    fun setRange(min: Float, max: Float): FloatOptionBuilder {
        return setRangeLimit(min, max)
    }
}

class DoubleOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : NumberOptionBuilder<Double, DoubleOptionBuilder>(loader, category, key, Codec.DOUBLE) {

    override fun self(): DoubleOptionBuilder {
        return this
    }

    fun setDefault(value: Double): DoubleOptionBuilder {
        return setDefaultValue(value)
    }

    fun setRange(min: Double, max: Double): DoubleOptionBuilder {
        return setRangeLimit(min, max)
    }
}

// TODO: Add Enum support
// TODO: Add List<?>/Set support
// TODO: Add (ARGB) color picker support