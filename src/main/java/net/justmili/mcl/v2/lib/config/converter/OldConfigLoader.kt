package net.justmili.mcl.v2.lib.config.converter

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.mojang.serialization.JsonOps
import net.justmili.mcl.v1.lib.util.Platform
import net.justmili.mcl.v2.lib.config.MConfigLogger
import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.config.options.ConfigOption
import java.nio.file.Path

class OldConfigLoader {
    private val converters: MutableList<OldConfigConverter> = ArrayList(1)
    private var readable: List<OldConfigConverter> = emptyList()

    fun add(oldPath: Path, deleteOldFile: Boolean) {
        converters.add(OldConfigConverter(Platform.CONFIG_DIR.resolve(oldPath), deleteOldFile))
    }

    fun importInto(root: CategoryItem) {
        readable = converters.filter { it.read() }
        if (readable.isNotEmpty()) importValues(root)
    }

    // Only old files that were read successfully are ever deleted
    fun deleteConverted() {
        for (converter in readable) {
            if (converter.deleteAfter) converter.delete()
        }
    }

    private fun importValues(category: CategoryItem) {
        for (item in category.getChildren()) {
            if (item is CategoryItem) {
                importValues(item)
            } else if (item is ConfigOption<*>) {
                val oldKey = item.oldKey ?: continue
                val value = findValue(oldKey) ?: continue

                if (!loadOldValue(item, value)) MConfigLogger.invalidValue(item, oldKey)
            }
        }
    }

    private fun findValue(oldKey: String): JsonElement? {
        for (converter in readable) {
            val value = converter.find(oldKey)
            if (value != null) return value
        }
        return null
    }

    // Falls back to text parsing so values stored as text (properties files, "5" instead of 5) still convert
    private fun loadOldValue(option: ConfigOption<*>, value: JsonElement): Boolean {
        return option.load(JsonOps.INSTANCE, value) || (value is JsonPrimitive && option.loadText(value.asString))
    }
}