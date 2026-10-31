package net.justmili.mcl.v2.lib.config.items

import net.justmili.mcl.v2.lib.config.options.ConfigOption

class CategoryItem(private val name: String, private var comment: String?) : ConfigItem {
    private val children: MutableList<ConfigItem> = ArrayList()

    fun comment(comment: String?) {
        this.comment = comment
    }

    fun name(): String {
        return name
    }

    fun comment(): String? {
        return comment
    }

    fun add(item: ConfigItem) {
        val itemName = nameOf(item)
        require(itemName == null || children.none {nameOf(it) == itemName}) {"Config option or category '$itemName' already exists in category '$name'."}
        children.add(item)
    }

    fun children(): MutableList<ConfigItem> {
        return children
    }

    private fun nameOf(item: ConfigItem): String? {
        return when (item) {
            is ConfigOption<*> -> item.key()
            is CategoryItem -> item.name()
            else -> null
        }
    }
}