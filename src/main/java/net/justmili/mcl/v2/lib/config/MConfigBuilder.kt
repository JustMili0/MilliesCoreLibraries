package net.justmili.mcl.v2.lib.config

import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.config.items.CommentItem
import net.justmili.mcl.v2.lib.config.options.*
import java.util.function.Consumer

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

        fun addBooleanOption(key: String): BooleanOptionBuilder {
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

        // TODO: Add Enum support
        // TODO: Add List<?>/Set support
        // TODO: Add (ARGB) color picker support

        fun build() {
            loader.buildOrLoad()
        }
    }

    class CategoryBuilder(loader: MConfigLoader, category: CategoryItem, private val child: CategoryItem) : ConfigBuilder(loader, category) {
        fun comment(comment: String): CategoryBuilder {
            child.comment(comment)
            return this
        }
    }
}