package net.justmili.mcl.v2.lib.config.items

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
        children.add(item)
    }

    fun children(): MutableList<ConfigItem> {
        return children
    }
}