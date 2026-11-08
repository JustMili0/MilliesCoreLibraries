package net.justmili.mcl.v2.lib.config.items

import net.justmili.mcl.v2.lib.config.options.ConfigOption

class CategoryItem(private val name: String, private var comment: String?) : ConfigItem {
    private val children: MutableList<ConfigItem> = ArrayList()

    fun getComment(): String? {
        return comment
    }

    fun setComment(comment: String?) {
        this.comment = comment
    }

    fun getName(): String {
        return name
    }

    fun getChildren(): MutableList<ConfigItem> {
        return children
    }

    fun add(item: ConfigItem) {
        val itemName = getItemName(item)
        if (itemName != null) {
            for (child in children) {
                require(getItemName(child) != itemName) { "Config option or category '$itemName' already exists in category '$name'." }
            }
        }
        children.add(item)
    }

    private fun getItemName(item: ConfigItem): String? {
        return when (item) {
            is ConfigOption<*> -> item.key
            is CategoryItem -> item.name
            else -> null
        }
    }
}
