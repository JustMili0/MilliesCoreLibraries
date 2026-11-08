package net.justmili.mcl.v2.lib.config.serializers

import net.justmili.mcl.v1.lib.util.common.FileIO
import net.justmili.mcl.v2.lib.config.MConfigLogger
import net.justmili.mcl.v2.lib.config.items.*
import net.justmili.mcl.v2.lib.config.options.ConfigOption
import java.io.IOException
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

object PropertiesIO : ConfigIO {

    override fun validate(path: Path, root: CategoryItem) {
        if (root.getChildren().any { it is CategoryItem }) MConfigLogger.unsupportedElement(path, "categories", "element not supported in .properties file format")
        checkForDupedKeys(path, root.getChildren(), HashSet())
    }

    override fun write(path: Path, root: CategoryItem): Boolean {
        return FileIO.write(path) { writer ->
            writer.write(ConfigIO.TOKEN_POUND)
            writer.write(path.fileName.toString())
            writer.write("\n\n")
            writeItems(writer, root.getChildren())
        }
    }

    override fun load(path: Path, root: CategoryItem): Boolean {
        val properties = Properties()
        try {
            Files.newBufferedReader(path).use { reader -> properties.load(reader) }
        } catch (e: IOException) {
            MConfigLogger.readFail(path, e.message)
            return false
        }

        loadItems(properties, root.getChildren())
        return true
    }

    private fun checkForDupedKeys(path: Path, items: List<ConfigItem>, keys: MutableSet<String>) {
        for (item in items) {
            if (item is CategoryItem) {
                checkForDupedKeys(path, item.getChildren(), keys)
            } else if (item is ConfigOption<*>) {
                require(keys.add(item.key)) {
                    "Duplicate config option '${item.key}' detected in '${path.fileName}' after flattening categories."
                }
            }
        }
    }

    private fun writeItems(writer: Writer, items: List<ConfigItem>) {
        for (item in items) {
            when (item) {
                is CategoryItem -> {
                    ConfigIO.eachCommentLine(item.getComment()) { text, from, to -> writeComment(writer, text, from, to) }
                    writeItems(writer, item.getChildren())
                }

                is CommentItem -> ConfigIO.eachCommentLine(item.text) { text, from, to -> writeComment(writer, text, from, to) }

                is ConfigOption<*> -> {
                    ConfigIO.eachCommentLine(item) { text, from, to -> writeComment(writer, text, from, to) }
                    writer.write(item.key)
                    writer.write("=")
                    writeEscaped(writer, item.toText())
                    writer.write("\n\n")
                }
            }
        }
    }

    private fun writeComment(writer: Writer, text: String, from: Int, to: Int) {
        writer.write(ConfigIO.TOKEN_POUND)
        writer.write(text, from, to - from)
        writer.write("\n")
    }

    private fun writeEscaped(writer: Writer, text: String) {
        var last = 0
        for (i in text.indices) {
            val escape = when (text[i]) {
                '\\' -> "\\\\"
                '\n' -> "\\n"
                '\r' -> "\\r"
                else -> continue
            }
            if (last < i) writer.write(text, last, i - last)
            writer.write(escape)
            last = i + 1
        }
        if (last < text.length) writer.write(text, last, text.length - last)
    }

    private fun loadItems(properties: Properties, items: List<ConfigItem>) {
        for (item in items) {
            if (item is CategoryItem) {
                loadItems(properties, item.getChildren())
            } else if (item is ConfigOption<*>) {
                val raw = properties.getProperty(item.key) ?: continue
                if (!item.loadText(raw)) MConfigLogger.invalidValue(item)
            }
        }
    }
}
