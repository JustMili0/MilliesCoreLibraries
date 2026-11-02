package net.justmili.mcl.v2.lib.config.writers.properties

import net.justmili.mcl.CoreLibs
import net.justmili.mcl.v1.lib.util.Platform
import net.justmili.mcl.v1.lib.util.common.FileIO
import net.justmili.mcl.v2.lib.config.MConfigLoader
import net.justmili.mcl.v2.lib.config.items.*
import net.justmili.mcl.v2.lib.config.options.ConfigOption
import net.justmili.mcl.v2.lib.config.writers.FileWriter
import java.io.BufferedWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class PropertiesIO : FileWriter {

    override fun write(path: Path, root: CategoryItem) {
        checkForDupedKeys(path, root.getChildren(), HashSet())
        FileIO.write(path) {writer ->
            writer.write(FileWriter.TOKEN_POUND + path.fileName + "\n\n")
            writeItems(writer, root.getChildren(), false)
        }
    }

    private fun writeItems(writer: BufferedWriter, items: List<ConfigItem>, warnedAboutCategories: Boolean) {
        var warned = warnedAboutCategories
        for (item in items) {
            when (item) {
                is CategoryItem -> {
                    if (!warned && Platform.IS_DEV_ENV) {
                        CoreLibs.LOGGER.warn("Categories are not supported in properties config format - they will not exist in the file.")
                        warned = true
                    }
                    writeDescComment(writer, item.getComment(), FileWriter.TOKEN_POUND, FileWriter.NO_INDENT)
                    writeItems(writer, item.getChildren(), warned)
                }

                is CommentItem -> writeDescComment(writer, item.text, FileWriter.TOKEN_POUND, FileWriter.NO_INDENT)
                is ConfigOption<*> -> {
                    writeOptionComment(writer, item, FileWriter.TOKEN_POUND, FileWriter.NO_INDENT)
                    writer.write(item.key + "=" + escape(item.toText()) + "\n\n")
                }
            }
        }
    }

    override fun load(path: Path, root: CategoryItem): Boolean {
        checkForDupedKeys(path, root.getChildren(), HashSet())
        val properties = Properties()
        try {
            Files.newBufferedReader(path).use {reader -> properties.load(reader)}
        } catch (e: IOException) {
            MConfigLoader.loadError(e.message)
            return false
        }

        loadItems(properties, root.getChildren())
        return true
    }

    private fun loadItems(properties: Properties, items: List<ConfigItem>) {
        for (item in items) {
            if (item is CategoryItem) {
                loadItems(properties, item.getChildren())
            } else if (item is ConfigOption<*>) {
                val raw = properties.getProperty(item.key)
                if (raw != null && !item.loadText(raw)) CoreLibs.LOGGER.warn("Invalid value '{}' for config option '{}', using default.", raw, item.key)
            }
        }
    }

    private fun checkForDupedKeys(path: Path, items: List<ConfigItem>, keys: MutableSet<String>) {
        for (item in items) {
            if (item is CategoryItem) {
                checkForDupedKeys(path, item.getChildren(), keys)
            } else if (item is ConfigOption<*>) {
                require(keys.add(item.key)) {"Duplicate config option '${item.key}' detected in '${path.fileName}' after removing categories (not supported in the properties format), duplicate keys are not allowed."}
            }
        }
    }

    private fun escape(text: String): String {
        return text.replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r")
    }
}