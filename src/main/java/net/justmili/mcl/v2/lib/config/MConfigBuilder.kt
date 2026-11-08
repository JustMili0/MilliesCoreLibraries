package net.justmili.mcl.v2.lib.config

import com.mojang.serialization.Codec
import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.config.items.CommentItem
import net.justmili.mcl.v2.lib.config.options.*
import java.nio.file.Path
import java.util.function.Consumer

class MConfigBuilder @JvmOverloads constructor(modId: String, customFileName: String? = null, configType: ConfigTypes, fileType: FileTypes, createSubDirectory: Boolean = false) {
    private val loader = MConfigLoader(modId, customFileName, configType, fileType, createSubDirectory)

    fun create(): ConfigBuilder {
        return ConfigBuilder(loader, loader.getRoot())
    }

    fun create(content: Consumer<ConfigBuilder>): ConfigBuilder {
        val root = create()
        content.accept(root)
        return root
    }

    fun reload(): Boolean {
        return loader.reload()
    }

    @JvmOverloads fun tryConvertOldConfig(oldPath: String, deleteOldFile: Boolean = true): MConfigBuilder {
        loader.addForConversion(Path.of(oldPath), deleteOldFile)
        return this
    }

    open class ConfigBuilder(private val loader: MConfigLoader, private val category: CategoryItem) {

        open fun addComment(comment: String): ConfigBuilder {
            category.add(CommentItem(comment))
            return this
        }

        fun addCategory(name: String, content: Consumer<ConfigBuilder>): CategoryBuilder {
            val child = CategoryItem(name, null)
            category.add(child)
            content.accept(ConfigBuilder(loader, child))
            return CategoryBuilder(loader, category, child)
        }

        fun addBoolOption(key: String): BooleanOptionBuilder {
            return BooleanOptionBuilder(loader, category, key)
        }

        fun addStringOption(key: String): StringOptionBuilder {
            return StringOptionBuilder(loader, category, key)
        }

        fun addIntOption(key: String): IntOptionBuilder {
            return IntOptionBuilder(loader, category, key)
        }

        fun addLongOption(key: String): LongOptionBuilder {
            return LongOptionBuilder(loader, category, key)
        }

        fun addFloatOption(key: String): FloatOptionBuilder {
            return FloatOptionBuilder(loader, category, key)
        }

        fun addDoubleOption(key: String): DoubleOptionBuilder {
            return DoubleOptionBuilder(loader, category, key)
        }

        fun <E : Enum<E>> addEnumOption(key: String, type: Class<E>): EnumOptionBuilder<E> {
            return EnumOptionBuilder(loader, category, key, type)
        }

        fun <T : Any> addOption(key: String, codec: Codec<T>): CodecOptionBuilder<T> {
            return CodecOptionBuilder(loader, category, key, codec)
        }

        fun build() {
            loader.buildOrLoad()
        }
    }

    class CategoryBuilder(loader: MConfigLoader, category: CategoryItem, private val child: CategoryItem) : ConfigBuilder(loader, category) {

        override fun addComment(comment: String): CategoryBuilder {
            child.setComment(child.getComment()?.let { it + "\n" + comment } ?: comment)
            return this
        }
    }
}