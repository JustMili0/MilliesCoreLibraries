package net.justmili.mcl.v2.lib.config

import com.mojang.serialization.Codec
import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.config.items.CommentItem
import net.justmili.mcl.v2.lib.config.options.*
import java.nio.file.Path
import java.util.function.Consumer

class MConfigBuilder @JvmOverloads constructor(modId: String, customFileName: String? = null, configType: ConfigTypes, fileType: FileTypes, createSubDirectory: Boolean = false) {
    private val loader = MConfigLoader(modId, customFileName, configType, fileType, createSubDirectory)

    fun create() = ConfigBuilder(loader, loader.root)

    fun create(content: Consumer<ConfigBuilder>): ConfigBuilder {
        val root = create()
        content.accept(root)
        return root
    }

    fun reload() = loader.reload()

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

        fun addBoolOption(key: String) = BooleanOptionBuilder(loader, category, key)
        fun addStringOption(key: String) = StringOptionBuilder(loader, category, key)
        fun addIntOption(key: String) = IntOptionBuilder(loader, category, key)
        fun addLongOption(key: String) = LongOptionBuilder(loader, category, key)
        fun addFloatOption(key: String) = FloatOptionBuilder(loader, category, key)
        fun addDoubleOption(key: String) = DoubleOptionBuilder(loader, category, key)
        fun <E : Enum<E>> addEnumOption(key: String, type: Class<E>) = EnumOptionBuilder(loader, category, key, type)
        fun addRgbColorOption(key: String) = RgbOptionBuilder(loader, category, key)
        fun addArgbColorOption(key: String) = ArgbOptionBuilder(loader, category, key)
        fun <T : Any> addOption(key: String, codec: Codec<T>) = CodecOptionBuilder(loader, category, key, codec)

        fun build() = loader.buildOrLoad()
    }

    class CategoryBuilder(loader: MConfigLoader, category: CategoryItem, private val child: CategoryItem) : ConfigBuilder(loader, category) {

        override fun addComment(comment: String): CategoryBuilder {
            child.comment = child.comment?.let { it + "\n" + comment } ?: comment
            return this
        }
    }
}