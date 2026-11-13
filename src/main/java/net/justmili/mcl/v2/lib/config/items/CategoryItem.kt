package net.justmili.mcl.v2.lib.config.items

import net.justmili.mcl.v2.lib.config.options.ConfigOption
import net.minecraft.network.chat.Component

class CategoryItem(val name: String, var comment: Component?) : ConfigItem {
    val children: MutableList<ConfigItem> = ArrayList()

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