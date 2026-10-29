package net.justmili.mcl.v2.lib.config

import com.mojang.serialization.Codec
import net.justmili.mcl.v2.lib.config.items.*
import java.nio.file.Path
import java.util.function.Consumer
import java.util.function.Predicate

class MConfigBuilder @JvmOverloads constructor(modId: String, customFileName: String? = null, configType: ConfigTypes, fileType: FileTypes, createSubDirectory: Boolean = false) {
    private val loader = MConfigLoader(modId, customFileName, configType, fileType, createSubDirectory)

    fun create(): ConfigBuilder {
        return ConfigBuilder(loader, loader.root())
    }

    open class ConfigBuilder(private val loader: MConfigLoader, private val category: CategoryItem) {
        fun addComment(comment: String): ConfigBuilder {
            category.add(CommentItem(comment))
            return this
        }

        fun addCategory(name: String, content: Consumer<ConfigBuilder>): CategoryBuilder {
            val child = CategoryItem(name, null)
            category.add(child)
            content.accept(ConfigBuilder(loader, child))
            return CategoryBuilder(loader, category, child)
        }

        fun <T : Any> addOption(key: String, value: T, setter: Consumer<T>, vararg flags: OptionFlags): OptionBuilder {
            return register(key, value, codecFor(value), {true}, if (value is Boolean) "Allowed values: true, false" else null, setter, *flags)
        }

        fun addOption(key: String, value: String, charLimit: Int, setter: Consumer<String>, vararg flags: OptionFlags): OptionBuilder {
            return register<String>(key, value, Codec.STRING, {candidate: String -> candidate.length <= charLimit}, "Maximum length: $charLimit", setter, *flags)
        }

        fun <T : Comparable<T>> addOption(key: String, value: T, min: T, max: T, setter: Consumer<T>, vararg flags: OptionFlags): OptionBuilder {
            return register(key, value, codecFor(value), {candidate: T -> candidate in min..max}, "Allowed range: $min-$max", setter, *flags)
        }

        // TODO: Add Enum support
        // TODO: Add List<?>/Set support
        // TODO: Add (ARGB) color picker support

        fun build() {
            loader.buildOrLoad()
        }

        private fun <T> register(key: String, value: T, codec: Codec<T>, validator: Predicate<T>, restriction: String?, setter: Consumer<T>, vararg flags: OptionFlags): OptionBuilder {
            val option = ConfigOption<T>(key, value, codec, validator, restriction, setter, *flags)
            category.add(option)
            return OptionBuilder(loader, category, option)
        }

        companion object {
            private fun <T : Any> codecFor(value: T): Codec<T> {
                val codec: Codec<*>
                when (value) {
                    is String -> codec = Codec.STRING
                    is Int -> codec = Codec.INT
                    is Long -> codec = Codec.LONG
                    is Float -> codec = Codec.FLOAT
                    is Double -> codec = Codec.DOUBLE
                    is Boolean -> codec = Codec.BOOL
                    else -> throw IllegalArgumentException("Unsupported config option type: " + value.javaClass.getName())
                }

                @Suppress("UNCHECKED_CAST")
                return codec as Codec<T>
            }
        }
    }

    class CategoryBuilder(loader: MConfigLoader, category: CategoryItem, private val child: CategoryItem) : ConfigBuilder(loader, category) {
        fun comment(comment: String): CategoryBuilder {
            child.comment(comment)
            return this
        }
    }

    class OptionBuilder(loader: MConfigLoader, category: CategoryItem, private val option: ConfigOption<*>) : ConfigBuilder(loader, category) {
        fun comment(comment: String): OptionBuilder {
            option.comment(comment)
            return this
        }

        fun attachImage(path: Path) {
            // TODO: Add optional attaching image paths for use in v2.configscreen
        }
    }
}