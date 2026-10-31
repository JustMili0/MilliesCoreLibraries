package net.justmili.mcl.v2.lib.config.options

import com.mojang.serialization.Codec
import net.justmili.mcl.v2.lib.config.MConfigLoader
import net.justmili.mcl.v2.lib.config.items.CategoryItem
import java.nio.file.Path
import java.util.function.Predicate

abstract class OptionBuilder<T : Any, B : OptionBuilder<T, B>>(private val loader: MConfigLoader, private val category: CategoryItem, private val key: String, private val codec: Codec<T>) {
    private var defaultValue: T? = null
    private var attachedImage: Path? = null
    private var comment: String? = null
    private var flags: Array<out OptionFlags> = emptyArray()

    protected abstract fun self(): B

    protected open fun validator(): Predicate<T> {
        return Predicate {true}
    }

    protected open fun restriction(): String? {
        return null
    }

    protected fun default(value: T): B {
        this.defaultValue = value
        return self()
    }

    fun setComment(comment: String): B {
        this.comment = comment
        return self()
    }

    fun setFlags(vararg flags: OptionFlags): B {
        this.flags = flags
        return self()
    }

    fun attachImage(path: Path): B {
        this.attachedImage = path
        return self()
    }

    fun buildOption(): ConfigOption<T> {
        val value = checkNotNull(defaultValue) {"Config option '$key' has no default value."}
        val validator = validator()
        require(validator.test(value)) {"Default value '$value' of config option '$key' does not meet its own limits."}

        val option = ConfigOption(key, value, codec, validator, restriction(), loader::save, *flags)
        val text = comment
        if (text != null) option.comment(text)

        category.add(option)
        return option
    }
}

class BooleanOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : OptionBuilder<Boolean, BooleanOptionBuilder>(loader, category, key, Codec.BOOL) {

    override fun self(): BooleanOptionBuilder {
        return this
    }

    override fun restriction(): String {
        return "Allowed values: true, false"
    }

    fun setDefault(value: Boolean): BooleanOptionBuilder {
        return default(value)
    }
}

class StringOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : OptionBuilder<String, StringOptionBuilder>(loader, category, key, Codec.STRING) {
    private var charLimit = Int.MAX_VALUE

    override fun self(): StringOptionBuilder {
        return this
    }

    override fun validator(): Predicate<String> {
        val limit = charLimit
        return Predicate {candidate: String -> candidate.length <= limit}
    }

    override fun restriction(): String? {
        return if (charLimit == Int.MAX_VALUE) null else "Maximum length: $charLimit"
    }

    fun setDefault(value: String): StringOptionBuilder {
        return default(value)
    }

    fun setCharLimit(limit: Int): StringOptionBuilder {
        require(limit >= 0) {"Character limit can't be negative."}
        this.charLimit = limit
        return this
    }
}

abstract class NumberOptionBuilder<T, B : NumberOptionBuilder<T, B>>(loader: MConfigLoader, category: CategoryItem, key: String, codec: Codec<T>) : OptionBuilder<T, B>(loader, category, key, codec) where T : Comparable<T>, T : Any {
    private var range: ClosedRange<T>? = null

    override fun validator(): Predicate<T> {
        val limit = range
        return Predicate {candidate: T -> limit == null || candidate in limit}
    }

    override fun restriction(): String? {
        val limit = range
        return if (limit == null) null else "Allowed range: ${limit.start} to ${limit.endInclusive}"
    }

    protected fun range(min: T, max: T): B {
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
        return default(value)
    }

    fun setRange(min: Int, max: Int): IntOptionBuilder {
        return range(min, max)
    }
}

class LongOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : NumberOptionBuilder<Long, LongOptionBuilder>(loader, category, key, Codec.LONG) {

    override fun self(): LongOptionBuilder {
        return this
    }

    fun setDefault(value: Long): LongOptionBuilder {
        return default(value)
    }

    fun setRange(min: Long, max: Long): LongOptionBuilder {
        return range(min, max)
    }
}

class FloatOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : NumberOptionBuilder<Float, FloatOptionBuilder>(loader, category, key, Codec.FLOAT) {

    override fun self(): FloatOptionBuilder {
        return this
    }

    fun setDefault(value: Float): FloatOptionBuilder {
        return default(value)
    }

    fun setRange(min: Float, max: Float): FloatOptionBuilder {
        return range(min, max)
    }
}

class DoubleOptionBuilder(loader: MConfigLoader, category: CategoryItem, key: String) : NumberOptionBuilder<Double, DoubleOptionBuilder>(loader, category, key, Codec.DOUBLE) {

    override fun self(): DoubleOptionBuilder {
        return this
    }

    fun setDefault(value: Double): DoubleOptionBuilder {
        return default(value)
    }

    fun setRange(min: Double, max: Double): DoubleOptionBuilder {
        return range(min, max)
    }
}